#!/bin/sh

case "$1" in
  start)
    /usr/bin/aesdsocket -d
    ;;
  stop)
    killall aesdsocket
    ;;
  restart)
    killall aesdsocket
    /usr/bin/aesdsocket -d
    ;;
  *)
    echo "Usage: $0 {start|stop|restart}"
    exit 1
esac

exit 0
