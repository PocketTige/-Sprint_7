package ru.juli.practicum.client;

public class TrackCredentials{
    private final int track;

    public TrackCredentials(int track) {
        this.track = track;
    }
    public int TrackCredentials() {
        return track;
    }
    public static TrackCredentials fromOrder(Order order) {
        return new TrackCredentials(order.getTrack());
    }
}