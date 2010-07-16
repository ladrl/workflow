package clamp.messages

import java.net.URI
import java.util.Date


class HashBlock(val algorithmName: String, val hash: Array[Byte])

case class UserCredentials(val id: String, val passwordHash: HashBlock)

class GateDescriptor(val id: URI, val name: Option[String], val description: Option[String])


// General message object to send inbetween Clamp actors
trait ClampMessage{
	val origin: URI
}

// Information message. It only states information and no response is necessary
case class ClampInformation(val origin: URI, val timeStamp: Date, val info: String) extends ClampMessage

// Reqest message. Needs an answer in the form of a ClampResponse
class ClampRequest(val origin: URI, val requester: UserCredentials) extends ClampMessage

class ClampResponse
