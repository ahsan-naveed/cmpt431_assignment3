/**
 * CMPT 431 Distributed Systems - Assignment 03
 *
 * GROUP:
 * Ahsan Naveed (anaveed)
 * Amandeep Sindhar (asindhar)
 */

// server
// Binds REP socket to tcp://*:5555
// Expects "time" from client, replies with current datetime

const zmq = require("zmq");

// socket to talk to clients
const responder = zmq.socket("rep");

responder.on("message", function(request) {
  console.log("Received request: [", request.toString(), "]");

  // send reply back to client.
  if (request.toString() === "time") {
    responder.send(new Date().toISOString());
  } else {
    responder.send("Invalid request.");
  }
});

responder.bind("tcp://*:5555", function(err) {
  if (err) {
    console.log(err);
  } else {
    console.log("Server is listening on 5555…");
    console.log("Waiting for client request...");
  }
});

process.on("SIGINT", function() {
  responder.close();
});
