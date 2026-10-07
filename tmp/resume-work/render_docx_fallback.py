from pathlib import Path

from docx import Document
from docx.table import Table
from docx.text.paragraph import Paragraph
from docx.oxml.ns import qn
from PIL import Image, ImageDraw, ImageFont


DOCX = Path(r"D:\Code\知光正版\zhiguang_be-main\output\简历1-项目1产品包装版.docx")
OUT_DIR = Path(r"D:\Code\知光正版\zhiguang_be-main\tmp\resume-work\qa-productized-fallback")
DPI = 144
EMU_PER_INCH = 914400
PT_TO_PX = DPI / 72
BODY_FONT = r"C:\Windows\Fonts\simfang.ttf"
BOLD_FONT = r"C:\Windows\Fonts\simhei.ttf"


def px_from_emu(value):
    return int(round(value / EMU_PER_INCH * DPI))


def font(size_pt, bold=False):
    return ImageFont.truetype(BOLD_FONT if bold else BODY_FONT, max(10, round(size_pt * PT_TO_PX)))


def wrap_text(draw, text, font_obj, max_width):
    if not text:
        return [""]
    lines = []
    current = ""
    for ch in text:
        candidate = current + ch
        if current and draw.textlength(candidate, font=font_obj) > max_width:
            lines.append(current.rstrip())
            current = ch.lstrip()
        else:
            current = candidate
    if current or not lines:
        lines.append(current.rstrip())
    return lines


def iter_blocks(document):
    for child in document.element.body.iterchildren():
        if child.tag == qn("w:p"):
            yield Paragraph(child, document)
        elif child.tag == qn("w:tbl"):
            yield Table(child, document)


def paragraph_size(paragraph, default=10):
    for run in paragraph.runs:
        if run.text and run.font.size:
            return run.font.size.pt
    return default


def paragraph_is_bold(paragraph):
    runs = [run for run in paragraph.runs if run.text]
    return bool(runs) and all(run.bold for run in runs)


class Renderer:
    def __init__(self, document):
        section = document.sections[0]
        self.page_w = px_from_emu(section.page_width)
        self.page_h = px_from_emu(section.page_height)
        self.left = px_from_emu(section.left_margin)
        self.right = px_from_emu(section.right_margin)
        self.top = px_from_emu(section.top_margin)
        self.bottom = px_from_emu(section.bottom_margin)
        self.content_w = self.page_w - self.left - self.right
        self.pages = []
        self.new_page()

    def new_page(self):
        image = Image.new("RGB", (self.page_w, self.page_h), "white")
        self.pages.append(image)
        self.image = image
        self.draw = ImageDraw.Draw(image)
        self.y = self.top

    def ensure(self, height):
        if self.y + height > self.page_h - self.bottom:
            self.new_page()

    def draw_paragraph(self, paragraph):
        text = paragraph.text
        if not text:
            self.y += 2
            return
        size = paragraph_size(paragraph, 10)
        is_bold = paragraph_is_bold(paragraph)
        fnt = font(size, is_bold)
        lines = wrap_text(self.draw, text, fnt, self.content_w)
        line_h = max(round(size * PT_TO_PX * 1.05), fnt.getbbox("国")[3] + 1)
        height = len(lines) * line_h + (4 if size >= 13 else 1)
        self.ensure(height)
        for line in lines:
            self.draw.text((self.left, self.y), line, fill="black", font=fnt)
            self.y += line_h
        self.y += 4 if size >= 13 else 1

    def draw_table(self, table):
        grid = [int(col.get(qn("w:w"))) for col in table._tbl.tblGrid.gridCol_lst]
        grid_total = sum(grid) or 1
        col_widths = [self.content_w * value / grid_total for value in grid]
        for row in table.rows:
            cells = []
            seen = []
            col_index = 0
            for cell in row.cells:
                tc = cell._tc
                if any(tc is existing for existing in seen):
                    col_index += 1
                    continue
                seen.append(tc)
                span = 1
                if tc.tcPr is not None and tc.tcPr.gridSpan is not None:
                    span = int(tc.tcPr.gridSpan.val)
                width = sum(col_widths[col_index:col_index + span])
                cells.append((cell, col_index, width))
                col_index += span

            max_height = 0
            prepared = []
            for cell, start_col, width in cells:
                inner_w = max(20, width - 10)
                lines_for_cell = []
                cell_height = 4
                for paragraph in cell.paragraphs:
                    size = paragraph_size(paragraph, 10)
                    bold = paragraph_is_bold(paragraph)
                    fnt = font(size, bold)
                    lines = wrap_text(self.draw, paragraph.text, fnt, inner_w)
                    line_h = max(round(size * PT_TO_PX * 1.02), fnt.getbbox("国")[3])
                    lines_for_cell.append((lines, fnt, line_h, paragraph.alignment))
                    cell_height += len(lines) * line_h + 1
                prepared.append((start_col, width, lines_for_cell, cell_height))
                max_height = max(max_height, cell_height)

            if row.height is not None:
                max_height = max(max_height, px_from_emu(row.height))
            self.ensure(max_height)
            row_top = self.y

            for start_col, width, paragraphs, _ in prepared:
                x = self.left + sum(col_widths[:start_col]) + 5
                inner_w = width - 10
                cy = row_top + 2
                for lines, fnt, line_h, alignment in paragraphs:
                    for line in lines:
                        text_w = self.draw.textlength(line, font=fnt)
                        if alignment is not None and int(alignment) == 1:
                            tx = x + max(0, (inner_w - text_w) / 2)
                        elif alignment is not None and int(alignment) == 2:
                            tx = x + max(0, inner_w - text_w)
                        else:
                            tx = x
                        self.draw.text((tx, cy), line, fill="black", font=fnt)
                        cy += line_h
                    cy += 1
            self.y += max_height
        self.y += 1


def main():
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    for old in OUT_DIR.glob("page-*.png"):
        old.unlink()
    document = Document(DOCX)
    renderer = Renderer(document)
    for block in iter_blocks(document):
        if isinstance(block, Paragraph):
            renderer.draw_paragraph(block)
        else:
            renderer.draw_table(block)
    for index, page in enumerate(renderer.pages, start=1):
        path = OUT_DIR / f"page-{index}.png"
        page.save(path)
        print(path)
    print(f"pages={len(renderer.pages)} final_y={renderer.y} usable_bottom={renderer.page_h - renderer.bottom}")


if __name__ == "__main__":
    main()
