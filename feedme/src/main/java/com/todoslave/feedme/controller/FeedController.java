//package com.todoslave.feedme.controller;
//
//import com.todoslave.feedme.DTO.CreatureTodoResponseDTO;
//import com.todoslave.feedme.DTO.FeedRequestDTO;
//import com.todoslave.feedme.service.FeedService;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/feed")
//@RequiredArgsConstructor
//public class FeedController {
//
//    private final FeedService feedService;
//
//    //피드 생성
//    @Operation(summary = "피드 생성")
//    @PostMapping("/feed")
//    public ResponseEntity<List<CreatureTodoResponseDTO>> createTodo(@RequestBody FeedRequestDTO feedRequestDTO) {
//
//
//
//    }
//
//    // 크리쳐 투두 생성
//    @Operation(summary = "크리쳐 투두 생성 / 있으면 생성 X")
//    @GetMapping("/{weather}")
//    public ResponseEntity<List<CreatureTodoResponseDTO>> createTodo(@PathVariable String weather) {
//        System.out.println("이거 맞아?");
//        List<CreatureTodoResponseDTO> list = new ArrayList<>();
//        list = creatureTodoService.insertTodo(weather);
//        return ResponseEntity.ok(list);
//    }
//
//
//}
