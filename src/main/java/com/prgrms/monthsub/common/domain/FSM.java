package com.prgrms.monthsub.common.domain;

import com.prgrms.monthsub.common.domain.FSM.Event;
import com.prgrms.monthsub.common.exception.global.FSMException.InvalidEvent;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class FSM<T extends Event> extends BaseEntity {

  private State state;

  public abstract FSM<T> transit(T event);

  public interface State {

    default State next(
      Event event,
      State state
    ) {
      if (!state.stateEvents().contains(event)) {
        System.out.println(state.stateEvents());
        throw new InvalidEvent();
      }
      
      return event.getNextState();
    }

    List stateEvents();
  }

  protected interface Event {
    State getNextState();
  }
}
