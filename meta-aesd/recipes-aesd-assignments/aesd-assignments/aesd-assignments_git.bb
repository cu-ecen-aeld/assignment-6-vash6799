# aesd-assignments_git.bb
# Builds aesdsocket from your assignment repo and installs it + autostarts via SysV init (BusyBox)

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SUMMARY = "AESD assignments - aesdsocket"
PV = "1.0+git${SRCPV}"

# Fetch your assignments repo (SSH). If CI/host SSH is annoying, switch protocol=ssh -> protocol=https
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-vash6799.git;protocol=ssh;branch=main \
           file://aesdsocket-start-stop.sh \
           file://aesdsocket.init \
          "

# Pin to a specific commit in your assignments repo
SRCREV = "7d517d88749e873c9cc1d31a1867ca42f82490f9"

# Build from the server directory inside the repo
S = "${WORKDIR}/git/server"

# SysV init auto-start (works with BusyBox init; no systemctl needed)
inherit update-rc.d
INITSCRIPT_NAME = "aesdsocket"
INITSCRIPT_PARAMS = "defaults"

# Package contents
FILES:${PN} += " \
    ${bindir}/aesdsocket \
    ${bindir}/aesdsocket-start-stop.sh \
    ${sysconfdir}/init.d/aesdsocket \
"

# Link flags (append so we don't clobber Yocto defaults)
TARGET_LDFLAGS:append = " -pthread -lrt"

do_configure() {
    :
}

do_compile() {
    oe_runmake
}

do_install() {
    # Install aesdsocket binary
    install -d ${D}${bindir}
    install -m 0755 ${S}/aesdsocket ${D}${bindir}/aesdsocket

    # Install optional start/stop helper script
    install -m 0755 ${WORKDIR}/aesdsocket-start-stop.sh ${D}${bindir}/aesdsocket-start-stop.sh

    # Install SysV init script (the boot hook)
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/aesdsocket.init ${D}${sysconfdir}/init.d/aesdsocket
}
