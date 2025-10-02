package com.project.fatcat;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.fatcat.catBoard.repository.BoardRepository;
import com.project.fatcat.catBoard.repository.PostRepository;
import com.project.fatcat.cats.repository.CatRepository;
import com.project.fatcat.entity.Cat;
import com.project.fatcat.entity.Inquiry;
import com.project.fatcat.entity.InquiryComment;
import com.project.fatcat.entity.KnowledgeBoard;
import com.project.fatcat.entity.KnowledgePost;
import com.project.fatcat.entity.User;
import com.project.fatcat.inquiry.repository.InquiryCommentRepository;
import com.project.fatcat.inquiry.repository.InquiryRepository;
import com.project.fatcat.users.repository.UserRepository;

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
	
	@Autowired
	private InquiryRepository inquiryRepository;
	
	@Autowired
	private InquiryCommentRepository inquiryCommentRepository;

	@Test
	void contextLoads() {

		
		
//		
//		// 1. 테스트 사용자 생성 (기존 DB 유저 조회로 변경)
//		// userSeq 1과 2를 가진 사용자가 이미 DB에 존재한다고 가정합니다.
//
//		// findById는 Optional<User>를 반환하므로 안전하게 처리해야 합니다.
//		User user1 = userRepository.findById(12) 
//		    .orElseThrow(() -> new IllegalStateException("DB에서 userSeq 1번 유저를 찾을 수 없습니다. 테스트 환경을 확인하세요."));
//
//		User user2 = userRepository.findById(2)
//		    .orElseThrow(() -> new IllegalStateException("DB에서 userSeq 2번 유저를 찾을 수 없습니다. 테스트 환경을 확인하세요."));
//
//		// 2. 문의 3개 생성 및 저장 (조회된 user 객체를 사용)
//		Inquiry inquiry1 = Inquiry.builder()
//		        .user(user1) // ✅ 이미 DB에 존재하는 user1 객체 사용
//		        .inquiryTitle("배송 문의: 주문한 사료가 오지 않았어요.")
//		        .inquiryContent("지난주에 주문했는데 아직도 배송 준비 중입니다. 확인 부탁드려요.")
//		        .viewCount(5)
//		        .createDate(LocalDateTime.now().minusDays(3))
//		        .isDeleted(false)
//		        .build();
//		inquiryRepository.save(inquiry1);
//
//		
		
		
//		
//		 // 1. 테스트 사용자 생성 (User 엔티티가 있다고 가정)
//        // User 엔티티가 없거나 다른 필드를 가진다면 이 부분을 수정해야 합니다.
//        User user1 = User.builder().userSeq(1).userEmail("role_user").build();
//        User user2 = User.builder().userSeq(2).userEmail("admin").build(); 
//        
//        // 실제 UserRepository를 사용하여 저장
//        userRepository.save(user1);
//        userRepository.save(user2);
//        
//
//        // 2. 문의 3개 생성 및 저장
//        
//        // 문의 1: 답변 예정
//        Inquiry inquiry1 = Inquiry.builder()
//                .user(user1) 
//                .inquiryTitle("배송 문의: 주문한 사료가 오지 않았어요.")
//                .inquiryContent("지난주에 주문했는데 아직도 배송 준비 중입니다. 확인 부탁드려요.")
//                .viewCount(5)
//                .createDate(LocalDateTime.now().minusDays(3))
//                .isDeleted(false)
//                .build();
//        inquiryRepository.save(inquiry1);

//        // 문의 2: 답변 대기
//        Inquiry inquiry2 = Inquiry.builder()
//                .user(user1) 
//                .inquiryTitle("결제 문의: 포인트 사용이 안됩니다.")
//                .inquiryContent("보유 포인트가 있는데 결제 시 사용 버튼이 활성화되지 않아요.")
//                .viewCount(2)
//                .createDate(LocalDateTime.now().minusDays(1))
//                .isDeleted(false)
//                .build();
//        inquiryRepository.save(inquiry2);
//        
//        // 문의 3: 삭제 예정 (목록에 노출되지 않음)
//        Inquiry inquiry3 = Inquiry.builder()
//                .user(user2) 
//                .inquiryTitle("관리자 테스트: 삭제 처리될 문의")
//                .inquiryContent("이 문의는 나중에 삭제됨으로 표시될 예정입니다.")
//                .viewCount(0)
//                .createDate(LocalDateTime.now())
//                .isDeleted(true) // 삭제 상태
//                .build();
//        inquiryRepository.save(inquiry3);
//        
//        System.out.println("--- 2. 문의 3개 저장 완료 ---");
//
//        // 3. 문의 1에 대한 답변 생성 및 저장 (관리자가 답변)
//        InquiryComment inquiryComment = InquiryComment.builder()
//                .inquiry(inquiry1) // 1번 문의에 연결
//                .user(user2)       // 관리자(user2)가 답변
//                .inquiryComment("고객님, 배송 지연으로 불편을 드려 죄송합니다. 오늘 바로 출고될 예정입니다.")
//                .createDate(LocalDateTime.now().minusDays(2))
//                .isDeleted(false)
//                .build();
//        inquiryCommentRepository.save(inquiryComment);
//        
//        // Inquiry 엔티티의 리스트에도 수동으로 추가 (Optional: 지연 로딩 문제 방지)
//        inquiry1.getInquiryCommentList().add(inquiryComment);
//        
//        System.out.println("--- 3. 답변 1개 저장 완료 ---");
//        
//        // 4. 데이터 확인 (Optional)
//        Optional<Inquiry> savedInquiry1 = inquiryRepository.findById(inquiry1.getInquirySeq());
//        assertThat(savedInquiry1).isPresent();
//        assertThat(savedInquiry1.get().getInquiryTitle()).isEqualTo(inquiry1.getInquiryTitle());
//        
//        long activeInquiryCount = inquiryRepository.findByIsDeletedFalse(null).getTotalElements();
//        System.out.println("--- 활성화된 문의 개수: " + activeInquiryCount + " ---");
//    }
		
		
		
		
		
		
		
		
//		
		// 1. 테스트를 위한 유저 정보 가져오기 (user_seq=2)
//		User user = userRepository.findById(12).orElseThrow();
//
//		// 2. 새로운 고양이 정보 저장 (INSERT)
//		// "수컷" 대신 Enum 타입인 Cat.Gender.MALE 사용
//		Cat newCat = Cat.builder()
//		.user(user) // 유저 정보는 이제 ID 2번을 사용합니다.
//		.catName("설이")
//		.catBirtthday(LocalDate.of(2021, 7, 10)) 
//		.catGender(Cat.Gender.FEMALE) // 'cat_gender' 필드를 올바른 Enum 값으로 설정
//		.catBreed("코리안 숏헤어") // 필수 필드인 종 이름도 추가
//		.isNeutered(true) // 필수 필드인 중성화 여부도 추가
//		.build();
//
//		Cat savedCat = catRepository.save(newCat);
//
		//*** MALE (수컷)**과 **FEMALE (암컷) **//
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//
//		// 고양이 정보 등록
//
		/*
		 * // 1. 테스트를 위한 유저 정보 가져오기 (user_seq=1) User user =
		 * userRepository.findById(1).orElseThrow();
		 * 
		 * // 2. 새로운 고양이 정보 저장 (INSERT) // String "수컷" 대신 Enum 타입인 Cat.Gender.MALE 사용
		 * Cat newCat = Cat.builder() .user(user) .catName("톰")
		 * .catBirtthday(LocalDate.of(2022, 7, 7)) .catGender(Cat.Gender.FEMALE) //
		 * 'cat_gender' 필드를 올바른 Enum 값으로 설정 .build();
		 * 
		 * Cat savedCat = catRepository.save(newCat);
		 * 
		 *//*** MALE (수컷)**과 **FEMALE (암컷) ***//*
												*/

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
//        board.setBoardCode("2");  // 1번 게시판 (수의사에게 질문)
//
//        KnowledgeBoard saved = boardRepository.save(board);
//
//        System.out.println("저장된 board_seq = " + saved.getBoardSeq());
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
//	    user.setRole("admin");
//	    user.setCreateDate(LocalDateTime.now());
//	    user.setIsDeleted(false);
//
//	    User saved = userRepository.save(user);
//	    System.out.println("저장된 userSeq = " + saved.getUserSeq());
//
//		
//		// 1. User 객체 생성 및 ROLE 설정
//        User user = new User();
//        user.setUserEmail("11111@001123.com");
//        // ⭐ 중요: 실제 서비스에서는 비밀번호를 반드시 암호화(BCrypt)해야 합니다.
//        user.setUserPassword("yj1123!!"); 
//        user.setUserName("김유정");
//        user.setNickname("관리자");
//        user.setPhoneNumber("01051250691");
//        user.setLatitude(37.5665);
//        user.setLongitude(126.9780);
//        
//        // Admin 역할 설정
//        user.set("admin"); // ⭐ Role을 admin으로 설정합니다.
//        
//        user.setCreateDate(LocalDateTime.now());
//        user.setIsDeleted(false);
//
//        // 2. DB에 저장
//        User saved = userRepository.save(user);
//        System.out.println("--- 저장된 admin userSeq = " + saved.getUserSeq() + " ---");
//    }
//		


}
}
