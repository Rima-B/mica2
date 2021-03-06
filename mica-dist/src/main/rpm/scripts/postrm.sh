#!/bin/sh
# postrm script for mica2
#

set -e

# summary of how this script can be called:
#        * <postrm> `remove'
#        * <postrm> `purge'
#        * <old-postrm> `upgrade' <new-version>
#        * <new-postrm> `failed-upgrade' <old-version>
#        * <new-postrm> `abort-install'
#        * <new-postrm> `abort-install' <old-version>
#        * <new-postrm> `abort-upgrade' <old-version>
#        * <disappearer's-postrm> `disappear' <overwriter>
#          <overwriter-version>
# for details, see http://www.debian.org/doc/debian-policy/ or
# the debian-policy package


case "$1" in
	[0-1])
    userdel -f mica || true
    rm -rf /var/lib/mica2 /var/log/mica2 /tmp/mica2 /etc/mica2 /usr/share/mica2*
  ;;

  *)
    echo "postrm called with unknown argument \`$1'" >&2
    exit 1
  ;;
esac

exit 0
