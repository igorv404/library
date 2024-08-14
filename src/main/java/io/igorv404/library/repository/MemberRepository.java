package io.igorv404.library.repository;

import io.igorv404.library.model.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
  List<Member> findAllByName(String name);
}
