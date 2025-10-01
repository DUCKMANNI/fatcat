package com.project.fatcat.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.fatcat.entity.enums.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
	    name = "users",
	    uniqueConstraints = {@UniqueConstraint(name = "uq_user_email", columnNames = {"userEmail"})}
	)
public class User implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userSeq;
	
	@Column(nullable = false, unique = true, length = 255)
    private String userEmail;   // ✅ 로그인 ID

    @Column(nullable = false, length = 255)
    private String userPassword; // ✅ 암호화된 비밀번호

    @Column(length = 50)
    private String userName; // 실제 이름
	
	@Column( nullable = false, length = 50)
	private String nickname;
	
	@Column(nullable = false, length = 20)
	private String phoneNumber;
	
	@Column( length = 200)
	private String address1;
	
	@Column(length = 200)
	private String address2;
	
	@Column(length = 20)
	private String zipCode;
	
	@Column(nullable = false)
	private Double latitude;
	
	@Column(nullable = false)
	private Double longitude;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private UserRole role;
	
	@Column(length = 50)
    private String userType;	//A ->냥집사, B ->예비집사
	
	private String profileImage;
	
	private String vetLicenseImage;
	
	@Column(insertable = false, updatable = false,
	        columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;
	
	private LocalDateTime lastLoginDate;
	
	@Column(insertable = false, nullable = false,
	        columnDefinition = "BOOLEAN DEFAULT 0")
	private Boolean isDeleted;
	
	// 👉 UserDetails 필수 메서드
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(userRole.name()));
//    }
	
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
	
	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductReview> productReviewList = new ArrayList<>();
	
	
	// --- ✅ UserDetails 메서드 구현부 ---
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return List.of(new SimpleGrantedAuthority(this.role.name()));
	}

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userEmail; // 로그인 ID = 이메일
    }

    public String getUserName() {
        return this.userName; // 진짜 이름
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부 (추후 필요하면 DB 컬럼으로 관리 가능)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return !this.isDeleted; // ✅ 탈퇴 계정이면 로그인 불가
    }

}