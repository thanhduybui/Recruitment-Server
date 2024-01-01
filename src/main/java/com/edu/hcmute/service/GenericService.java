package com.edu.hcmute.service;


import com.edu.hcmute.response.ServiceResponse;

import java.security.Provider;

public interface GenericService<T, K> {
    ServiceResponse getAll(Boolean isAll);
    ServiceResponse getOne(K id);
    ServiceResponse create(T object);
    ServiceResponse update(T object);
    ServiceResponse delete(K id);
}
