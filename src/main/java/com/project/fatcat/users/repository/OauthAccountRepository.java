package com.project.fatcat.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.OauthAccount;

public interface OauthAccountRepository extends JpaRepository<OauthAccount, Integer>{

	Optional<OauthAccount> findByProviderAndProviderUserId(String provider, String providerUserId);
}
