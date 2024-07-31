package com.todoslave.feedme.domain.entity.communication;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "memberchatroomchecked")
public class MemberChatRoomChecked {

  @Id
  private String id;
  private String memberChatRoomId;
  private String memberId;
  private LocalDateTime recentCheckTime;

}
