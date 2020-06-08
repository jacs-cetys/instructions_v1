package com.cetys.instructions.dao;
/*****
 *   Created by Jose Armando Cardenas
 *   on 07/06/2020
 */

import com.cetys.instructions.model.Request;
import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Long> {
}
