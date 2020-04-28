#!/usr/bin/env bash

echo `date`
###bash best practice###
set -o errexit
set -o pipefail
set -o nounset
# set -o xtrace

# Set magic variables for current file & dir
__dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
__file="${__dir}/$(basename "${BASH_SOURCE[0]}")"
__base="$(basename ${__file} .sh)"

#parse the env file
if [ -f ./env ]
then
	source ./env
else
  echo "Missing env file, please create."
	exit -1
fi

###bash best practice###

#update first
sudo yum -y install epel-release
sudo yum config-manager --set-enabled PowerTools
sudo yum update -y

#install dependencies
sudo yum install -y python3
sudo pip3 install tornado==4.5.3

#add a ssh key to the root user if desired
if [[ -v SSHKEY ]]
then
  mkdir -p ~/.ssh
  echo -e $SSHKEY > ~/.ssh/authorized_keys
fi

#add pre-generated public/private keys to the root user
if [[ -v PRIVATE_KEY && -v PUBLIC_KEY ]]
then
  echo -e $PRIVATE_KEY > ~/.ssh/id_rsa
  echo -e $PUBLIC_KEY > ~/.ssh/id_rsa.pub
  chmod 600 ~/.ssh/id_rsa
  chmod 644 ~/.ssh/id_rsa.pub
fi

#install and configure salt
sudo yum install -y https://repo.saltstack.com/py3/redhat/salt-py3-repo-latest.el8.noarch.rpm
sudo yum install -y salt-*

echo "$JUMPHOST_IP salt" >> /etc/hosts
#git checkout the salt repository
if [[ -v PRIVATE_KEY && -v PUBLIC_KEY ]]
then
  if [[ -d /srv/salt ]]
  then
    rm -rf /srv/salt
  fi
  mkdir /srv/salt
  cd /srv/salt
  git clone git@gitlab.com:sme-inc/javelin/bootstrap-k8s/2-salt-modules.git 
fi
#create a grains configuration
cat > /etc/salt/minion.d/grains.conf << EOF
grains:
  roles:
    - jumphost
EOF

systemctl enable salt-master.service
systemctl start salt-master.service
systemctl enable salt-minion.service
systemctl start salt-minion.service

#install and configure kickstart
sudo yum install -y dnsmasq syslinux tftp-server vsftpd

#configure DNS
mv /etc/dnsmasq.conf  /etc/dnsmasq.conf.backup
cat > /etc/dnsmasq.conf << EOF
interface=ens192,lo
#bind-interfaces
domain=stark.columbia.sme-corp.net
# DHCP range-leases
dhcp-range=ens192,10.2.1.100,10.2.1.253,255.255.255.0,1h
# PXE - Hard coded IP of our jumphost.sh VM
dhcp-boot=pxelinux.0,pxeserver,10.2.1.101
# Gateway
dhcp-option=3,10.2.1.0
# DNS
dhcp-option=6,10.2.1.1
server=10.2.1.1
# Broadcast Address
dhcp-option=28,10.0.0.255
# NTP Server
dhcp-option=42,0.0.0.0

pxe-prompt="Press F8 for menu.", 60
pxe-service=x86PC, "Install CentOS 8 from network server 10.2.1.0", pxelinux
enable-tftp
tftp-root=/var/lib/tftpboot
EOF

cp -r /usr/share/syslinux/* /var/lib/tftpboot

cp ~/jumphost/anaconda-ks.cfg /var/ftp/pub/
chmod 755 /var/ftp/pub/anaconda-ks.cfg

if [[ -d /var/lib/tftpboot/pxelinux.cfg ]]
then
  rm -rf /var/lib/tftpboot/pxelinux.cfg
fi
mkdir /var/lib/tftpboot/pxelinux.cfg
touch /var/lib/tftpboot/pxelinux.cfg/default

cat > /var/lib/tftpboot/pxelinux.cfg/default << EOF
default menu.c32
prompt 0
timeout 300
ONTIMEOUT local

menu title ########## PXE Boot Menu ##########

label 1
menu label ^1) Install CentOS 8 x64 with Kickstart
kernel centos8/vmlinuz
append initrd=centos8/initrd.img inst.ks=ftp://10.2.1.101/pub/anaconda-ks.cfg

label 2
menu label ^2) Boot from local drive
EOF

#Download Centos iso if not exist
cd ~
if [[ ! -f ./CentOS-8.1.1911-x86_64-dvd1.iso ]]
then
  wget http://mirror.umd.edu/centos/8.1.1911/isos/x86_64/CentOS-8.1.1911-x86_64-dvd1.iso
fi

mount -o loop ~/CentOS-8.1.1911-x86_64-dvd1.iso /mnt
mkdir /var/lib/tftpboot/centos8
cp /mnt/images/pxeboot/vmlinuz  /var/lib/tftpboot/centos8
cp /mnt/images/pxeboot/initrd.img  /var/lib/tftpboot/centos8

rsync -avhW --no-compress --progress /mnt/*  /var/ftp/pub/ 
rsync -avhW --no-compress --progress /mnt/.[^.]*  /var/ftp/pub/ 

chmod -R 755 /var/ftp/pub

sed -i 's/anonymous_enable=NO/anonymous_enable=YES/g' /etc/vsftpd/vsftpd.conf

systemctl enable dnsmasq
systemctl enable vsftpd

systemctl start dnsmasq
systemctl start vsftpd

firewall-cmd --add-service=ftp --permanent  	## Port 21
firewall-cmd --add-service=dns --permanent  	## Port 53
firewall-cmd --add-service=dhcp --permanent  	## Port 67
firewall-cmd --add-port=69/udp --permanent  	## Port for TFTP
firewall-cmd --add-port=4011/udp --permanent  ## Port for ProxyDHCP
firewall-cmd --add-port=4505-4506/tcp --permanent ##Port for Salt
firewall-cmd --reload  ## Apply rules

umount /mnt
echo `date`
