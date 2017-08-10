# Akka Actor Basics Tour

## Akka

Akka toolkit, a set of open-source libraries for designing scalable, resilient systems 
that span processor cores and networks. It is built by Lightbend to provide a simpler, 
single programming model - one way of coding for concurrent and distributed applications
- the actor programming model.
 
Akka allows you to focus on meeting business needs instead of writing low-level 
code to provide reliable behavior, fault tolerance, and high performance.

Common practices and programming models do not address important challenges 
inherent in designing systems for modern computer architectures. To be successful, 
distributed systems must cope in an environment where components crash without 
responding, messages get lost without a trace on the wire, and network latency 
fluctuates. These problems occur regularly in carefully managed intra-datacenter 
environments - even more so in virtualized architectures.

To deal with these realities, Akka provides:

- Multi-threaded behavior without the use of low-level concurrency constructs 
like atomics or locks. We do not even need to think about memory visibility issues.
- Transparent remote communication between systems and their components. You do 
not need to write or maintain difficult networking code.
- A clustered, high-availability architecture that is elastic, scales in or out, 
on demand.

All of these features are available through a uniform programming model: Akka is 
centered with and exploits the actor model to provide a level of abstraction 
that makes it easier to write correct concurrent, parallel and distributed 
systems. The actor model spans the set of Akka libraries, providing us with a 
consistent way of understanding and using them.

## Actor Model

Actors were invented decades ago by Carl Hewitt.

The actor model provides an abstraction that allows we to think about our code 
in terms of communication, like people in a large organization. The basic 
characteristic of actors is that they model the world as stateful entities 
communicating with each other by explicit message passing.

As computational entities, actors have these characteristics:

- They communicate with asynchronous messaging instead of method calls
- They manage their own state
- When responding to a message, they can:
  - Create other (child) actors
  - Send messages to other actors
  - Stop (child) actors or themselves

Actors are decoupled on three axes:
- Space: An actor gives no guarantee and has no expectation about where another actor is located.
- Time: An actor gives no guarantee and has no expectation about when its work will be done.
- Interface: An actor has no defined interface. An actor has no expectation 
about which messages other components can understand. Nothing is shared between 
actors; actors never point to or use a shared piece of information that changes 
in place. Information is passed in messages.

More info on slides `src/main/tut/index.html`.
