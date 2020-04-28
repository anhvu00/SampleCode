<h2>JUMPHOST</h2>


<p>Anh - 4/27/20
</p>
<h2>PURPOSE</h2>


<ul>

<li>How to set up a kickstart server/host to install Centos to another host (aka installation by script). 

<li>Source code is in the project jumphost on GitLab (git@gitlab.com:sme-inc/javelin/bootstrap-k8s/jumphost.git).

<li>This document shows the step-by-step to run it in our ESXi environment (aka “stark”).

<li>Intended audience is developers who want to do DevOps.
</li>
</ul>
<h2>OVERVIEW</h2>


<p>
The idea is to use Terraform to create a shell VM (no OS) on ESXi. When it is started, Kickstart would download and install a Centos OS on it. The Kickstart server/host is called the source VM which may be on the same ESXi network as the target VM, or on your local machine as long as it can “see” the target VM.  
</p>
<p>
This document describes the process of creating both VMs and testing them.
</p>
<h2>MATERIALS NEEDED</h2>


<ol>

<li>Admin access to stark ESXi environment to create/edit/delete/snapshot VMs

<li>Create two VMs: one is for Kickstart (source), the other is the target to be modified on start up. The script jumphost.sh will set up the source VM to be the Kickstart server/host.

<li>Root access to the source VM. We install Centos 8, edit and run the jumphost.sh script here. The target VM is only a “shell” (i.e. defined CPUs, RAM, and hard drive but no OS). The target OS will be installed on start up.

<li>Both VMs need to have network/internet access.
</li>
</ol>
<h2>SETUP VMs</h2>


<p>
I use an ESXi admin account to set up both Target and Source VM via ESXi web console. The set up includes:
</p>
<ul>

<li>The script needs some environment variables (SSHKEY, JUMPHOST_IP, PUBLIC_KEY, and PRIVATE_KEY). The SSHKEY is for cloning codes from GitLab. GitLab uses either ed25519 or RSA type of encryption so the SSHKEY must be of those types. 

<li>Run the jumphost.sh script which calls a lot of system commands without “sudo”. So, you need to be root.
</li>
</ul>
<p>
The source VM will be modified by the jumphost.sh script. So, you should create a snapshot of the original state immediately after creation and before running the script. This will save you a lot of time during development and testing.
</p>
<h3>TARGET VM</h3>


<p>
Ultimately, we will use Terraform to create all target VMs. This manual set up is for developing and testing purposes.
</p>
<p>
Use the ESXi web console to create a linux VM with the following specifications as a guideline:
</p>
<ul>

<li>4 CPUs, 8 GB RAM, 80 GB hard drive, linux server.

<li>Turn on network/internet

<li>On ESXi Edit Settings | VM Options | Boot Option, select “BIOS (Recommended)” firmware to boot this VM.
</li>
</ul>
<h3>SOURCE VM</h3>


<p>
After completion, this VM serves as the “installer” for all new target VM shells. When the new target VMs are started, their “BIOS (Recommended)” setting will access the source VM where a KickStart process will install the OS. Considering it like you mount the Centos.iso to an IP instead of a CD/DVD device. The jumphost.sh will set up the source VM to be the “installer”.
</p>
<p>
Provision this VM in ESXi is a 2-step process. First, you create a blank/shell VM with a mount point to a Centos iso (located on ESXi as a datasource folder). Then, on startup, the OS installation is run.
</p>
<p>
After a reboot, log in as root. We need to create a set of private, public key pair
</p>
<ul>

<li>In a terminal, use ifconfig to identify the IP address of this VM (label “ens192”). It should be something like 10.2.1.1xx. 

<li>Use command “ssh-keygen -t rsa” to generate a priv/pub key pair. I use all the default parameters, no password. The result is 2 files: ~/.ssh/id_rsa and id_rsa.pub.

<li>Use the ESXi menu to create a snapshot of this VM in case you need to run the jumphost.sh script on it again.
</li>
</ul>
<p>
Setup environment variables SSHKEY, JUMPHOST_IP, PRIVATE_KEY, and PUBLIC_KEY in the “env” file (see env.sample on GitLab, jumphost project).
</p>
<ul>

<li>JUMPHOST= the ip address of the source VM (i.e. 10.2.1.1xx)

<li>SSHKEY is your public key on the source VM. Copy and paste the public key hexadecimal text from id_xxx.pub to SSHKEY=”” in the env file. Also, paste it to the SSH key page on SME GitLab (i.e. add a new ssh key to your GitLab account). The script needs to clone a library from GitLab via SSH and GitLab needs this key to authenticate the download. You can test GitLab by “git clone” a project via ssh link or “ssh -T <a href="mailto:git@gitlab.com">git@gitlab.com</a>”.

<li>Delete the PRIVATE_KEY and PUBLIC_KEY from your “env” file. 
</li>
</ul>
<p>
Note:
</p>
<p>
The jumphost.sh script copies these values into your ~/.ssh/id_rsa and id_rsa.pub to ensure authentication to GitLab. Though logical, authentication will fail after a reboot. We need more time to investigate the usage of these environment variables. So, I recommend deleting them from the “env” file.
</p>
<h2>INTEGRATION TEST</h2>


<p>
Log in the source VM as root. We download the source code from GitLab and run it.
</p>
<ul>

<li>At root home directory, “git clone <a href="mailto:git@gitlab.com">git@gitlab.com</a>:sme-inc/javelin/bootstrap-k8s/jumphost.git”

<li>As of 4/25/20, my code is still in the “develop” branch, check it out if it is not merged to master yet.

<li>Copy “env.sample” to “env”. Make sure it contains the correct JUMPHOST_IP of the source VM and SSHKEY contain the correct public key of the source VM (i.e. ~/.ssh/id_xxx.pub)

<li>In jumphost.sh, check the PXE IP to match the source VM IP ( i.e. “dhcp-boot=pxelinux.0,pxeserver,<strong>10.2.1.101</strong>” and “append initrd=centos8/initrd.img inst.ks=ftp://<strong>10.2.1.101</strong>/pub/anaconda-ks.cfg”). PXE (Pre eXecution Environment, pronounced Pixie) is network booting.

<li>In anaconda-ks.cfg, check the ftp host and /etc/hosts content to match with the source VM IP address (i.e. “url --url=ftp://<strong>10.2.1.101</strong>/pub” and “echo "<strong>10.2.1.101</strong> salt" >> /etc/hosts”).

<li>Test access to GitLab with “ssh -T <a href="mailto:git@gitlab.com">git@gitlab.com</a>”.

<li>Cd ~/jumphost, run ./jumphost.sh. This takes at least 30 minutes.

<li>Reboot. Source VM is now a kickstart host.

<li>On ESXi web console, start the target VM. Hit F8 to access the installation menu. Select Centos 8 and it will install. Done.
</li>
</ul>
<h2>KNOWN ISSUES</h2>



<table>
  <tr>
   <td><strong>ISSUES</strong>
   </td>
   <td><strong>NOTES</strong>
   </td>
  </tr>
  <tr>
   <td>Gitlab authentication failed
   </td>
   <td>GitLab cannot authenticate your public/private ssh key. Verify if PRIVATE_KEY and PUBLIC_KEY in env file are necessary (TBD/TODO).
   </td>
  </tr>
  <tr>
   <td>Invalid command: “sudo yum config-manager”
   </td>
   <td>Use “sudo yum-config-manager”. Might relate to Centos 7. Use Centos 8.
   </td>
  </tr>
  <tr>
   <td>Command failed: git clone 2-salt-modules
   </td>
   <td>Remove the period at the end of the git clone 2-salt-modules command.
   </td>
  </tr>
  <tr>
   <td>Salt installation doesn’t work in Centos 7 (bootstrap-salt.sh -M git 2019.2.23 as root). Error: Package: zeromq-4.3.1-4.el8.x86_64 (salt-py3-latest) Requires: libstdc++.so.6(GLIBCXX_3.4.21)(64bit)
   </td>
   <td>Use Centos 8
   </td>
  </tr>
  <tr>
   <td>Hard coded IPs and lack of checking if file/folder existed.
   </td>
   <td>Future enhancement
   </td>
  </tr>
  <tr>
   <td>All default passwords are readable. 
   </td>
   <td>Future enhancement. Encrypt passwords in the jumphost.sh script.
   </td>
  </tr>
</table>

