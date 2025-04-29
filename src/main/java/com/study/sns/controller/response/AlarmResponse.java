package com.study.sns.controller.response;

import com.study.sns.model.Alarm;
import com.study.sns.model.AlarmArgs;
import com.study.sns.model.AlarmType;
import com.study.sns.model.User;
import com.study.sns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
public class AlarmResponse {
    private Long id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private String text;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getArgs(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getRegisteredAt(),
                alarm.getUpdatedAt(),
                alarm.getDeletedAt()
        );
    }

}
