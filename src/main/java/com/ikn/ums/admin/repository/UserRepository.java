package com.ikn.ums.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ikn.ums.admin.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByEmail(String email);
	
	@Modifying
    @Query("Update User SET otpCode=:otp WHERE email=:username")
	void saveOtp(String username, int otp);
    
    @Query("SELECT COUNT(*) FROM User WHERE email=:email and otpCode=:otp")
    Integer validateUserOtp(String email, int otp);
    
    @Query("UPDATE User SET encryptedPassword=:encodedPassword WHERE email=:email")
    @Modifying
    Integer updatePassword(String email, String encodedPassword);
    
    @Query("SELECT COUNT(*) FROM User WHERE email=:email ")
    Integer validateEmail(String email);
    
    @Modifying
    @Query("UPDATE User SET twoFactorAuthentication=:isOn WHERE email=:email")
    Integer updateTwofactorAuthenticationStatus(String email, boolean isOn);
    
//    @Modifying
//    @Query("UPDATE User SET profilePic=:profilePic WHERE email=:email")
//    UserDetailsEntity updateProfilePicByEmail(String email);
    
    @Query("SELECT email FROM User WHERE isActive=:isActive")
	List<String> findAllActiveUsersEmailIdList(boolean isActive);
    
    @Modifying
    @Query("DELETE FROM User WHERE email=:emailId")
    void deleteUserByUserId(String emailId);
}
