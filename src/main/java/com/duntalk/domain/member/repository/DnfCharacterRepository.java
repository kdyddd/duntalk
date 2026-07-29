package com.duntalk.domain.member.repository;

import com.duntalk.domain.member.entity.DnfCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DnfCharacterRepository extends JpaRepository<DnfCharacter, String> {

    Optional<DnfCharacter> findByMember_Id(Integer memberId);

}
