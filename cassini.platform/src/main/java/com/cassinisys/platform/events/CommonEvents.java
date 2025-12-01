package com.cassinisys.platform.events;

import com.cassinisys.platform.model.col.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

public final class CommonEvents {

    @Data
    @AllArgsConstructor
    public static class CommentAdded {
        private Comment comment;
    }

    @Data
    @AllArgsConstructor
    public static class SendPushNotificationOnCommentAdd {
        private Comment comment;
    }
}
