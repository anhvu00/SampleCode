package com.kyron.server;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

//public interface QueryRepository extends JpaRepository<MyDto, Long>, JpaSpecificationExecutor<MyDto>{
public interface QueryRepository extends JpaRepository<MyDto, Long> {
	@Query(value = "select c.customer_id id, c.first_name name, a.address from customer c, address a where c.address_id = a.address_id",
            nativeQuery = true)
	public List<MyDto> findByNameNative();
}
