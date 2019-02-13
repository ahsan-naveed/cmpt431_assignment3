// client
// Connects REQ socket to tcp://localhost:5555
// Asks server current datetime.

const zmq = require("zmq");
const dateFormat = require("dateformat");
const cp = require("child_process");

// socket to talk to server
console.log("Connecting to server…");
const requester = zmq.socket("req");

requester.connect("tcp://localhost:5555");

// start timer before sending request
const start = new Date().getTime();

// sends request to the server
console.log("Sending request …");
requester.send("time");

// process server's response
requester.on("message", function(reply) {
  // end timer after getting the reponse
  const end = new Date().getTime();

  // compute RTT in ms
  const rtt = end - start;
  const halfOfRtt = Math.ceil(rtt / 2);

  console.log("Received reply", ": [", reply.toString(), "]");

  // compute client's time i.e. tc = ts + dsc
  const serverTime = new Date(reply.toString()).getTime();
  const clientTime = serverTime + halfOfRtt;

  // format the new client time as month:day:hour:min:year.sec
  // ex 0206180219.59
  const clientDateTime = new Date(clientTime);
  const formattedTime = dateFormat(clientDateTime, "mmddHHMMyy.ss");
  const command = `sudo date ${formattedTime}`;

  // Set the client time
  console.log("Setting client time to server time...\n");
  cp.execSync(command);

  // log client-server communication
  console.log(`Client time without rtt: ${serverTime} ms`);
  console.log(`rtt: ${rtt} ms`);
  console.log(`rtt/2: ${halfOfRtt} ms`);
  console.log(`Client time with rtt: ${clientTime} ms\n`);

  // print client time after sync and server time for comparison
  console.log("After clock synchronization:");
  console.log(
    `Server time: ${dateFormat(
      new Date(serverTime),
      "dddd, mmmm dS, yyyy, h:MM:ss TT"
    )}`
  );
  console.log(
    `Client time: ${dateFormat(
      clientDateTime,
      "dddd, mmmm dS, yyyy, h:MM:ss TT"
    )}`
  );

  // close the connection
  requester.close();
  process.exit(0);
});

process.on("SIGINT", function() {
  requester.close();
});
