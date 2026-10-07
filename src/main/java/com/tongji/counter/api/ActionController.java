package com.tongji.counter.api;

import com.tongji.counter.api.dto.ActionRequest;
import com.tongji.counter.service.CounterService;
import com.tongji.auth.token.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 行为接口：点赞/取消点赞、收藏/取消收藏。
 *
 * <p>所有接口从 JWT 获取登录用户，返回操作是否改变状态以及当前状态值。
 * 调用链为 Controller → CounterService → Redis 分片 Bitmap 原子切换 → Kafka 增量事件 →
 * Redis 聚合 Hash/SDS；重复点赞或取消不会产生重复增量。</p>
 */
@RestController
@RequestMapping("/api/v1/action")
public class ActionController {

    // 执行位图幂等切换、状态查询和计数事件发布
    private final CounterService counterService;
    // 从 JWT uid 声明提取操作用户，避免请求体伪造 userId
    private final JwtService jwtService;

    public ActionController(CounterService counterService, JwtService jwtService) {
        this.counterService = counterService;
        this.jwtService = jwtService;
    }

    /**
     * 点赞操作。
     * @param req 目标实体类型和实体 ID
     * @param jwt 当前登录用户令牌
     * @return changed 表示本次是否真正置位，liked 表示操作后的事实状态
     */
    @PostMapping("/like")
    public ResponseEntity<Map<String, Object>> like(@Valid @RequestBody ActionRequest req,
                                                    @AuthenticationPrincipal Jwt jwt) {
        long uid = jwtService.extractUserId(jwt);
        boolean changed = counterService.like(req.getEntityType(), req.getEntityId(), uid);
        return ResponseEntity.ok(Map.of(
                "changed", changed, // 标识这次操作是否改变状态（避免重复点击）
                "liked", counterService.isLiked(req.getEntityType(), req.getEntityId(), uid)
        ));
    }

    /**
     * 取消点赞操作。
     * @param req 目标实体类型和实体 ID
     * @param jwt 当前登录用户令牌
     * @return changed 表示本次是否真正清位，liked 表示操作后的事实状态
     */
    @PostMapping("/unlike")
    public ResponseEntity<Map<String, Object>> unlike(@Valid @RequestBody ActionRequest req,
                                                      @AuthenticationPrincipal Jwt jwt) {
        long uid = jwtService.extractUserId(jwt);
        boolean changed = counterService.unlike(req.getEntityType(), req.getEntityId(), uid);
        return ResponseEntity.ok(Map.of(
                "changed", changed, // 状态是否发生变化
                "liked", counterService.isLiked(req.getEntityType(), req.getEntityId(), uid)
        ));
    }

    /**
     * 收藏操作。
     * @param req 目标实体类型和实体 ID
     * @param jwt 当前登录用户令牌
     * @return changed 表示本次是否真正置位，faved 表示操作后的事实状态
     */
    @PostMapping("/fav")
    public ResponseEntity<Map<String, Object>> fav(@Valid @RequestBody ActionRequest req,
                                                   @AuthenticationPrincipal Jwt jwt) {
        long uid = jwtService.extractUserId(jwt);
        boolean changed = counterService.fav(req.getEntityType(), req.getEntityId(), uid);
        return ResponseEntity.ok(Map.of(
                "changed", changed, // 状态是否发生变化
                "faved", counterService.isFaved(req.getEntityType(), req.getEntityId(), uid)
        ));
    }

    /**
     * 取消收藏操作。
     * @param req 目标实体类型和实体 ID
     * @param jwt 当前登录用户令牌
     * @return changed 表示本次是否真正清位，faved 表示操作后的事实状态
     */
    @PostMapping("/unfav")
    public ResponseEntity<Map<String, Object>> unfav(@Valid @RequestBody ActionRequest req,
                                                     @AuthenticationPrincipal Jwt jwt) {
        long uid = jwtService.extractUserId(jwt);
        boolean changed = counterService.unfav(req.getEntityType(), req.getEntityId(), uid);
        return ResponseEntity.ok(Map.of(
                "changed", changed, // 状态是否发生变化
                "faved", counterService.isFaved(req.getEntityType(), req.getEntityId(), uid)
        ));
    }
}
