package io.igorv404.library.controller;

import io.igorv404.library.model.Member;
import io.igorv404.library.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
  private final MemberService memberService;

  @GetMapping
  public List<Member> findAll() {
    return memberService.findAll();
  }

  @GetMapping("/{id}")
  public Member findById(@PathVariable Integer id) {
    return memberService.findById(id);
  }

  @PostMapping
  public Member save(@RequestBody String memberName) {
    return memberService.save(memberName);
  }

  @PatchMapping("/{id}")
  public Member update(@PathVariable Integer id, @RequestBody String memberName) {
    return memberService.update(id, memberName);
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable Integer id) {
    return memberService.delete(id);
  }
}
