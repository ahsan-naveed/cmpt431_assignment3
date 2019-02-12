## Summary

1. For sending a packet via UDP, we need a message to send, its length, ipaddress of destination, port of destination.
2. Once we have all these 4 things, we can create the socket object for carrying the data packets b/w client and server.
3. We invoke `send()`/`receive()` call for actually sending/recieving packets on both client and server sides.
4. The extract the data from the received packet using `getData()` method.

## References

1. https://www.geeksforgeeks.org/working-udp-datagramsockets-java/
