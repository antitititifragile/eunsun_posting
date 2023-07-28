package com.eunsunzzang.posting.member.security;

import org.springframework.data.repository.CrudRepository;
import com.eunsunzzang.posting.member.security.Token;

public interface TokenRepository extends CrudRepository<Token, Long> {
}
