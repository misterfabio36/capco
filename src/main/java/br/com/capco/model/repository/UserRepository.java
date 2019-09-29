package br.com.capco.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.capco.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
