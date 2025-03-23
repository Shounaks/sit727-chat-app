package org.shounak.sit727chatapp.repository;

import org.shounak.sit727chatapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
