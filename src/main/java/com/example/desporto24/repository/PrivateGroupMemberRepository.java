package com.example.desporto24.repository;
import com.example.desporto24.model.PrivateGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface PrivateGroupMemberRepository extends JpaRepository<PrivateGroupMember,Long> {
    List<PrivateGroupMember> findByUsernameIgnoreCaseOrderByGroupIdDesc(String username);
    List<PrivateGroupMember> findByGroupIdOrderByJoinedAtAsc(Long groupId);
    Optional<PrivateGroupMember> findByGroupIdAndUsernameIgnoreCase(Long groupId,String username);
    boolean existsByGroupIdAndUsernameIgnoreCase(Long groupId,String username);
    long countByGroupId(Long groupId);
}
