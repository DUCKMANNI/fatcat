package com.project.fatcat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.fatcat.catBoard.repository.BoardRepository;
import com.project.fatcat.catBoard.repository.PostRepository;
import com.project.fatcat.cats.repository.CatRepository;
import com.project.fatcat.entity.Cat;
import com.project.fatcat.entity.KnowledgeBoard;
import com.project.fatcat.entity.KnowledgePost;
import com.project.fatcat.entity.User;
import com.project.fatcat.users.UserRepository;

@SpringBootTest
class FatcatApplicationTests {
	
	
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CatRepository catRepository;
	
	

	@Test
	void contextLoads() {
		
		
//		//고양이 정보 등록
//		
//		  // 1. 테스트를 위한 유저 정보 가져오기 (user_seq=1)
//        User user = userRepository.findById(1).orElseThrow();
//
//        // 2. 새로운 고양이 정보 저장 (INSERT)
//       
//        Cat newCat = Cat.builder()
//                .user(user)
//                .catName("제리")
//                .catBirtthday(LocalDate.of(2022, 7, 7))
//                .build();
//        
//        Cat savedCat = catRepository.save(newCat);
//        
        
       
//        // 저장된 고양이의 ID로 다시 조회
//        Cat foundCat = catRepository.findById(savedCat.getCatSeq()).orElseThrow();
//        
//        // 정보 변경
//        foundCat.setCatName("애용이");
//        foundCat.setCatBirtthday(LocalDate.of(2023, 1, 20));
//        foundCat.setCatImageUrl("https://test.com/aeong.jpg");
//        
//        Cat updatedCat = catRepository.save(foundCat);
//        System.out.println("수정된 고양이의 catSeq: " + updatedCat.getCatSeq());
//        System.out.println("수정된 고양이 이름: " + updatedCat.getCatName());
//    }
		
		
		
//		// post
//		// 1. User와 Board 가져오기 (이미 테스트용으로 넣어둔 데이터)
//        User user = userRepository.findById(1).orElseThrow(); // user_seq = 1
//        KnowledgeBoard board = boardRepository.findById(1).orElseThrow(); // board_seq = 1
//
//        // 2. KnowledgePost 생성
//        KnowledgePost post = KnowledgePost.builder()
//                .knowledgeBoard(board)
//                .user(user)
//                .post_title("헤헤~~")
//                .post_content("별거 없어~~ 너따위~~!!")
//                .view_count(0)
//                .like_count(0)
//                .create_date(LocalDateTime.now())
//                .is_deleted(false)
//                .build();
//
//        // 3. 저장
//        KnowledgePost saved = postRepository.save(post);
//
//        System.out.println("저장된 post_seq = " + saved.getPost_seq());
//
//		
//		// board
//		KnowledgeBoard board = new KnowledgeBoard();
//        board.setBoard_code("1");  // 1번 게시판 (수의사에게 질문)
//
//        KnowledgeBoard saved = boardRepository.save(board);
//
//        System.out.println("저장된 board_seq = " + saved.getBoard_seq());
//		
//		
//		
//		// user
//	    User user = new User();
//	    user.setUserEmail("dbwjd112316@naver.com");
//	    user.setUserPassword("yj1123!!");
//	    user.setUserName("김유정");
//	    user.setNickname("관리왕");
//	    user.setPhoneNumber("01051250691");
//	    user.setLatitude(37.5665);
//	    user.setLongitude(126.9780);
//	    user.setRole("ADMIN");
//	    user.setCreateDate(LocalDateTime.now());
//	    user.setIsDeleted(false);
//
//	    User saved = userRepository.save(user);
//	    System.out.println("저장된 userSeq = " + saved.getUserSeq());
		
		
	

		
		
		
	}

}
