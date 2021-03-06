#!/bin/sh
set -e

# link mica folder to default python lib, /usr/share/pyshared may not be icluded in the lib path
python_lib=$(python -c "from distutils.sysconfig import get_python_lib; print(get_python_lib())")

case "$1" in
  0)
    if [ -d $python_lib ]; then
      rm -f $python_lib/mica
    fi
  ;;

  [1-2])
    if [ -d $python_lib ]; then
      rm -f $python_lib/mica
    fi
    ln -s /usr/share/pyshared/mica $python_lib/mica
  ;;

  *)
    echo "postinst called with unknown argument \`$1'" >&2
    exit 1
  ;;
esac
exit 0
