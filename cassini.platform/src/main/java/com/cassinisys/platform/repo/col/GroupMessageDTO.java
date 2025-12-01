package com.cassinisys.platform.repo.col;

import com.cassinisys.platform.model.col.GroupMessage;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by lakshmi on 6/4/2016.
 */
public class GroupMessageDTO {

    private Long messageCount;

    private Long activeUsersCount;

    private Page<GroupMessage> groupMessages;

    private List<GroupMessage> listGroupMessages;


    public Long getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Long messageCount) {
        this.messageCount = messageCount;
    }

    public Page<GroupMessage> getGroupMessages() {
        return groupMessages;
    }

    public void setGroupMessages(Page<GroupMessage> groupMessages) {
        this.groupMessages = groupMessages;
    }

    public Long getActiveUsersCount() {
        return activeUsersCount;
    }

    public void setActiveUsersCount(Long activeUsersCount) {
        this.activeUsersCount = activeUsersCount;
    }

    public List<GroupMessage> getListGroupMessages() {
        return listGroupMessages;
    }

    public void setListGroupMessages(List<GroupMessage> listGroupMessages) {
        this.listGroupMessages = listGroupMessages;
    }
}
