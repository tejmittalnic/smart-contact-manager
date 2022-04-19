package com.smart.smartcontactmanager.dao;

import com.smart.smartcontactmanager.entities.Contact;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact,Integer> {
    
}
