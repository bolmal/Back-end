package com.example.bolmalre.alarm.infrastructure;

import com.example.bolmalre.alarm.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm,Long> {
}
