package com.ikn.ums.users.repository;

import java.util.List;

import javax.persistence.Column;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ikn.ums.users.entity.UserDetailsEntity;

@Repository
public interface UserRepository extends JpaRepository<UserDetailsEntity, Integer> {
	
	UserDetailsEntity findByEmail(String email);
	
	@Modifying
    @Query("Update UserDetailsEntity SET otpCode=:otp WHERE email=:username")
	void saveOtp(String username, int otp);
    
    @Query("SELECT COUNT(*) FROM UserDetailsEntity WHERE email=:email and otpCode=:otp")
    Integer validateUserOtp(String email, int otp);
    
    @Query("UPDATE UserDetailsEntity SET encryptedPassword=:encodedPassword WHERE email=:email")
    @Modifying
    Integer updatePassword(String email, String encodedPassword);
    
    @Query("SELECT COUNT(*) FROM UserDetailsEntity WHERE email=:email ")
    Integer validateEmail(String email);
    
    @Modifying
    @Query("UPDATE UserDetailsEntity SET twoFactorAuthentication=:isOn WHERE email=:email")
    Integer updateTwofactorAuthenticationStatus(String email, boolean isOn);
    
//    @Modifying
//    @Query("UPDATE UserDetailsEntity SET profilePic=:profilePic WHERE email=:email")
//    UserDetailsEntity updateProfilePicByEmail(String email);
    
    @Query("SELECT email FROM UserDetailsEntity WHERE isActive=:isActive")
	List<String> findAllActiveUsersEmailIdList(boolean isActive);
}
