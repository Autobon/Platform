package com.autobon.platform.listener;

import com.autobon.cooperators.entity.Cooperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created by dave on 16/3/30.
 */
@Component
public class CooperatorEventListener {
    private static final Logger log = LoggerFactory.getLogger(CooperatorEventListener.class);

    @EventListener
    public void onCooperatorEvent(Event<Cooperator> event) {
        if (event.getAction() == Event.Action.CREATED) this.onCooperatorCreated(event.getData());
        else if (event.getAction() == Event.Action.VERIFIED) this.onCooperatorVerified(event.getData());
    }

    private void onCooperatorCreated(Cooperator cooper) {

    }

    private void onCooperatorVerified(Cooperator cooper) {

    }
}
