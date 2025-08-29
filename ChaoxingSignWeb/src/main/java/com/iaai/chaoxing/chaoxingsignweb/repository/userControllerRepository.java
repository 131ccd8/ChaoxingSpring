package com.iaai.chaoxing.chaoxingsignweb.repository;

import com.iaai.chaoxing.chaoxingsignweb.entity.userController;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface userControllerRepository extends JpaRepository<userController, Long> {
    boolean existsByUser(String user);

    @Query("SELECT u.pass FROM userController u WHERE u.user = :username")
    String findPassByUser(@Param("username") String username);

    @Query("SELECT u.pass FROM userController u WHERE u.user = :username")
    String findCookieByUser(@Param("username") String username);

    @Query("SELECT UNIX_TIMESTAMP(u.time) FROM userController u WHERE u.user = :username")
    Long findTimesByUser(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM userController u WHERE u.user = :username")
    void deleteByUsername(@Param("username") String username);
}