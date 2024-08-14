package io.igorv404.library.service;

import io.igorv404.library.exception.MemberHasBorrowedBooksException;
import io.igorv404.library.model.Member;
import io.igorv404.library.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  public List<Member> findAll() {
    return memberRepository.findAll();
  }

  public Member findById(Integer id) {
    return memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public Member save(String memberName) {
    return memberRepository.save(new Member(memberName));
  }

  public Member update(Integer id, String memberName) {
    Member existingMember = findById(id);
    existingMember.setName(memberName);
    return memberRepository.save(existingMember);
  }

  public String delete(Integer id) {
    Member existingMember = findById(id);
    if (existingMember.getBorrowedBooks().isEmpty()) {
      memberRepository.deleteById(id);
    } else {
      throw new MemberHasBorrowedBooksException();
    }
    return String.format("Member \"%s\" was deleted", existingMember.getName());
  }
}
