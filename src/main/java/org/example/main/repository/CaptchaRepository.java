package org.example.main.repository;

import org.example.main.model.CaptchaCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCodes, Integer> {

}
