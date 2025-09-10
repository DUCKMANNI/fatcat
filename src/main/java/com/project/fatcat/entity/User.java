package com.project.fatcat.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userSeq;
	
	@Column(length = 255)
	private String userEmail;
	
	@Column( length = 255)
	private String userPassword;
	
	@Column(length = 50)
	private String userName;
	
	@Column( nullable = false, length = 50)
	private String nickname;
	
	@Column(nullable = false, length = 20)
	private String phoneNumber;
	
	@Column( length = 200)
	private String address1;
	
	@Column(length = 200)
	private String address2;
	
	@Column(nullable = false)
	private Double latitude;
	
	@Column(nullable = false)
	private Double longitude;
	
	@Column(nullable = false, length = 20)
	private String role;
	
	private String vetLicenseImage;
	
	@Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENTTIMESTAMP")
	private LocalDateTime createDate;
	
	private LocalDateTime lastLoginDate;
	
	@Column( nullable = false, columnDefinition = "BOOLEAN DEFAULT 0")
	private Boolean isDeleted;
	
	@Builder.Default
	@OneToMany(mappedBy = "user")
    private List<OauthAccount> oauthAccountsList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Cat> catsList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<ShoppingCart> shoppingCartsList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Order> ordersList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Payment> paymentsList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserCoupon> userCouponsList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<VetReview> vetReviewsList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<VetReviewLike> vetReviewLikesList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<CareServiceBoard> careServiceBoardsList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "ownerUser")
    private List<CareSession> careSessionOwnerList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "sitterUser")
    private List<CareSession> careSessionSitterList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "authorUser")
    private List<CareServiceReview> careServiceReviewAuthorList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "targetUser")
    private List<CareServiceReview> careServiceReviewTargetList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<CareChatHistory> careChatHistorysList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<KnowledgePost> knowledgePostsList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<KnowledgeComment> knowledgeCommentsList = new ArrayList<>();

	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Inquiry> inquiryList = new ArrayList<>();
	
	@Builder.Default
    @OneToMany(mappedBy = "user")
    private List<InquiryComment> inquiryCommentsList = new ArrayList<>();

}