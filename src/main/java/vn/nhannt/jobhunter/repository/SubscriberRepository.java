package vn.nhannt.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.nhannt.jobhunter.domain.entity.Subscriber;

@Repository
public interface SubscriberRepository extends
        JpaRepository<Subscriber, Long>,
        JpaSpecificationExecutor<Subscriber> {
}
