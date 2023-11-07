package com.ead.authuser.services.impl;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.enums.ActionType;
import com.ead.authuser.models.User;
import com.ead.authuser.publishers.UserEventPublisher;
import com.ead.authuser.respositories.UserRepository;
import com.ead.authuser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;
    public UserServiceImpl(UserRepository userRepository, UserEventPublisher userEventPublisher) {
        this.userRepository = userRepository;
        this.userEventPublisher = userEventPublisher;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Page<User> findAll(Specification<User> spec, Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public User saveUser(User user){
        user = create(user);
        userEventPublisher.publishUserEvent(user.convertToUserEventDto(), ActionType.CREATE);
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        delete(user);
        userEventPublisher.publishUserEvent(user.convertToUserEventDto(), ActionType.DELETE);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        user = create(user);
        userEventPublisher.publishUserEvent(user.convertToUserEventDto(), ActionType.UPDATE);
        return user;
    }

    @Override
    @Transactional
    public User updatePassword(User user) {
        return create(user);
    }
}
