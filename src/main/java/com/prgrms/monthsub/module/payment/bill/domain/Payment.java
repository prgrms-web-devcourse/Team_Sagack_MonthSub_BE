package com.prgrms.monthsub.module.payment.bill.domain;

import static java.util.Collections.emptySet;

import com.prgrms.monthsub.common.domain.FSM;
import com.prgrms.monthsub.module.payment.bill.domain.Payment.Event;
import com.prgrms.monthsub.module.payment.bill.domain.Payment.State;
import com.prgrms.monthsub.module.series.series.domain.Series;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payment")
public class Payment extends FSM<State, Event> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "user_id", columnDefinition = "BIGINT", nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "state", columnDefinition = "VARCHAR(50)", nullable = false)
  private State state;

  @OneToMany(mappedBy = "payment", cascade = CascadeType.PERSIST)
  private final List<PaymentStateHistory> histories = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "series_id", referencedColumnName = "id")
  private Series series;

  @Builder
  private Payment(
    Long userId,
    Series series,
    State state
  ) {
    this.userId = userId;
    this.series = series;
    this.state = state;
  }

  @Override
  public Payment transit(Event event) {
    State nextState = (State) FSM.State.next(this.state, event);
    this.state = nextState;
    this.histories.add(new PaymentStateHistory(nextState, event, userId, series, this));

    return this;
  }


  public enum State implements FSM.State<Event> {
    NULL(Set.of(Event.PAY_REQUIRED)),
    PAY_CONFIRMED(Set.of(Event.PAY_ACCEPTED, Event.PAY_REJECTED)),
    PAY_CANCELED(emptySet()),
    PAY_COMPLETE(Set.of(Event.REFUND_REQUIRED)),
    REFUND_CONFIRMED(Set.of(Event.REFUND_ACCEPTED, Event.REFUND_REJECTED)),
    REFUND_COMPLETE(emptySet());

    final Set<Event> events;

    private State(Set<Event> events) {
      this.events = events;
    }

    @Override
    public Set<Event> getEvents() {
      return events;
    }
  }

  public enum Event implements FSM.Event<State> {
    PAY_REQUIRED {
      @Override
      public State getNextState() {
        return State.PAY_CONFIRMED;
      }
    },
    PAY_ACCEPTED {
      @Override
      public State getNextState() {
        return State.PAY_COMPLETE;
      }
    },
    PAY_REJECTED {
      @Override
      public State getNextState() {
        return State.PAY_CANCELED;
      }
    },
    REFUND_REQUIRED {
      @Override
      public State getNextState() {
        return State.PAY_CONFIRMED;
      }
    },
    REFUND_ACCEPTED {
      @Override
      public State getNextState() {
        return State.PAY_COMPLETE;
      }
    },
    REFUND_REJECTED {
      @Override
      public State getNextState() {
        return State.PAY_CONFIRMED;
      }
    };
  }

  public PaymentStateHistory getLastHistory() {
    if (this.histories.isEmpty()) {
      return null;
    }

    int lastIndex = this.histories.size() - 1;

    return this.histories.get(this.histories.size() - 1);
  }

}
