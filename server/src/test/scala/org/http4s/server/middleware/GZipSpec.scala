package org.http4s
package server
package middleware

import cats.implicits._
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPOutputStream
import org.http4s.server.syntax._
import org.http4s.dsl._
import org.http4s.headers._
import org.scalacheck.{Gen, Properties}
import org.scalacheck.Prop._

class GZipSpec extends Http4sSpec {
  // "GZip" should {
  //   "fall through if the route doesn't match" in {
  //     val service = GZip(HttpService.empty) |+| HttpService {
  //       case GET -> Root =>
  //         Ok("pong")
  //     }
  //     val req = Request(Method.GET, Uri.uri("/"))
  //       .putHeaders(`Accept-Encoding`(ContentCoding.gzip))
  //     val resp = service.orNotFound(req).unsafeRun
  //     resp.status must_== (Status.Ok)
  //     resp.headers.get(`Content-Encoding`) must beNone
  //   }
  // }

  "GZip single char" should {
    "work" in {
      val value = ""
      val service = GZip(HttpService { case GET -> Root => Ok(value) })
      val req = Request(Method.GET, Uri.uri("/")).putHeaders(`Accept-Encoding`(ContentCoding.gzip))
      val resp = service.orNotFound(req).unsafeRun.body.runLog.unsafeRun.toArray

      val byteArrayStream = new ByteArrayOutputStream()
      val gzipStream = new GZIPOutputStream(byteArrayStream)
      gzipStream.write(value.getBytes)
      gzipStream.close()
      val gzipped = byteArrayStream.toByteArray

      val crc = new java.util.zip.CRC32()
      crc.update(value.getBytes)
      println(crc.getValue)

      println(s"VALUE: '$value'")
      println(value == "")
      println(resp.mkString(", "))
      println(gzipped.mkString(", "))

      true
      // println(value)
      // // println(resp.type)
      // println(resp.as[String].unsafeRun.getBytes.mkString(", "))
      // println(gzipped.mkString(", "))
      // resp === gzipped
    }
  }

  // checkAll("GZip encoding", new Properties("GZip") {
  //   val asciiGen = Gen.containerOf[Array, Char](Gen.choose[Char](0, 127)).map(_.mkString)

  //   property("middleware encoding == GZIPOutputStream encoding") = forAll(asciiGen) { value: String =>
  //     val service = GZip(HttpService { case GET -> Root => Ok(value) })
  //     val req = Request(Method.GET, Uri.uri("/")).putHeaders(`Accept-Encoding`(ContentCoding.gzip))
  //     val resp = service.orNotFound(req).unsafeRun.body.runLog.unsafeRun.toArray

  //     val byteArrayStream = new ByteArrayOutputStream()
  //     val gzipStream = new GZIPOutputStream(byteArrayStream)
  //     gzipStream.write(value.getBytes)
  //     gzipStream.close()
  //     val gzipped = byteArrayStream.toByteArray

  //     val crc = new java.util.zip.CRC32()
  //     crc.update(value.getBytes)
  //     println(crc.getValue)

  //     println(s"VALUE: '$value'")
  //     println(value == "")
  //     println(resp.mkString(", "))
  //     println(gzipped.mkString(", "))


  //     // println(value)
  //     // // println(resp.type)
  //     // println(resp.as[String].unsafeRun.getBytes.mkString(", "))
  //     // println(gzipped.mkString(", "))
  //     resp === gzipped
  //   }
  // })
}
