class EventService {
    private val events = ArrayList<Event>()
    private val usersForEvent = HashMap<User, ArrayList<Event>>()

    fun addEvent(event : Event) {
        events.add(event)
    }

    fun getEvents(): ArrayList<Event> {
        return events
    }

    fun subscribe(subscribe: Pair<User, Event>) {
        if (usersForEvent.containsKey(subscribe.first)) {
            usersForEvent[subscribe.first]?.add(subscribe.second)
        } else {
            usersForEvent[subscribe.first] = arrayListOf(subscribe.second)
        }
    }

    fun getUsersForEvent(): HashMap<User, ArrayList<Event>> {
        return usersForEvent
    }
}