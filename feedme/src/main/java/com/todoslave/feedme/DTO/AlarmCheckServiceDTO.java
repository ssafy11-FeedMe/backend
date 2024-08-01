package com.todoslave.feedme.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlarmCheckServiceDTO {

  int memberId;
  private LocalDateTime checkTime;

}
