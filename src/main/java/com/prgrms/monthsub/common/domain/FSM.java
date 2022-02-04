package com.prgrms.monthsub.common.domain;

import com.prgrms.monthsub.common.domain.FSM.Event;
import com.prgrms.monthsub.common.domain.FSM.State;
import com.prgrms.monthsub.common.exception.global.FSMException.InvalidEvent;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class FSM<S extends State, E extends Event> extends BaseEntity {

  public abstract FSM<S, E> transit(E event);

  protected interface State<E extends Event> {
    Set<E> getEvents();

    static <E extends Event> State<E> next(
      State<E> state,
      E event
    ) {
      if (!state.getEvents().contains(event)) {
        throw new InvalidEvent();
      }

      return event.getNextState();
    }
  }

  protected interface Event<S extends State> {
    S getNextState();
  }
}
