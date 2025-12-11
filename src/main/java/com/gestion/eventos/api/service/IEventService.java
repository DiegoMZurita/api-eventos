package com.gestion.eventos.api.service;


import com.gestion.eventos.api.domain.Event;

import java.util.List;

public interface IEventService {
    List<Event> findAll();
}
