
<h2>AUTO-CREATE VM</h2>
<p>(Draft)
</p>
<p>Anh - 4/6/20
</p>
<h2>PURPOSE</h2>
<ul>
<li>Write scripts to create and update VMs on corporate networks (VMWare
hypervisor type 1, not the cloud). VirtualBox is hypervisor type 2 which may or
may not apply here.
<li>We want to focus on Ansible, Terraform, and ESXi.
<li>The intended audience is developers who want to do DevOps
</li>
</ul>
<h2>MATERIALS NEEDED</h2>
<p>
We need privileged access to our corporate resources: VPN, ESXi.
</p>
<ol>
<li>OpenVPN software + credentials to log in https://vpn.columbia.sme-corp.net
<li>Login credentials to corporate ESXi (as root or sysadmin). This allows you
to create/edit/delete VMs/resources. (TBD: we might need vSphere/vCenter for
terraform to work)
</li>
</ol>
<p>
We need some knowledge of Ansible, Terraform, and ESXi.
</p>
<h2>OPENVPN</h2>
<ul>
<li>Download, install OpenVPN
<li>Create ‘profile’ from a template (i.e. <a
href="https://vpn.columbia.sme-corp.net">https://vpn.columbia.sme-corp.net</a>)
<li>Change default password
</li>
</ul>
<p>
Note:
</p>
<p>
When you need to connect to another profile, you have to close/exit OpenVPN
completely to clear the cache. Otherwise, you’ll get an authentication error.
</p>
<h2>ESXI</h2>
<p>
VSphere is a suite of applications, including vCenter and ESXi. vSphere is like
Microsoft Office which contains many subcomponents such as Word and Excel.
</p>
<p>
We access our ESXi via a web console (i.e.<a
href="https://stark.columbia.sme-corp.net/ui/#/host/login">https://stark.columbia.sme-corp.net/ui/#/host/login</a>).
We can manage VMs, Storage, and Network from the web console.
</p>
<p>
 vCenter offers a unique feature to clone and convert VMs to binary templates
which can be used to create other subsequent VMs. There are many sample
Terraform scripts to clone such a VMDK template on the web. However, we don’t
have vCenter. The best we could do is using Terraform to create a blank shell VM
without an OS. (perhaps Ansible can install OS? We have Kickstart jumphost.sh
script, too).
</p>
<h2>TERRAFORM</h2>
<p>
Terraform is an orchestration tool. It can run a script to provision/create some
resource/VM. Consider the following variables:
</p>
<ul>
<li>Accounts and passwords
<li>Trusted keys for SSH
<li>DHCP or static IP, firewall settings.
<li>VM specs such as CPUs, RAM, and disk space.
</li>
</ul>
<p>
Put them in a ‘variable’ file for DevOps to configure (i.e. secret.tfvars,
production.tfvars)
</p>
<p>
vSphere = vCenter + ESXi + other components (need licenses). vCenter can convert
an existing VM (i.e. a baseline VM) to a template but we don’t have vCenter. We
only have ESXi.
</p>
<p>
Terraform is written in GoLang, a declarative language. The whole tool is just
one executable file. Add it to the path or /usr/bin and you are ready to go.
</p>
<ul>
<li>Declare platform, resources, and other configuration in text files (i.e.
*.tf, *.tfvars). Order of declaration does not matter, except in some special
cases like loop variables. Terraform takes care of dependencies.
<li>The common workflow to run Terraform includes 3 commands: init, plan, and
apply. A less common one is destroy. Type “terraform help” for more options.
</li>
</ul>
<p>
The text file/declaration script contains:
</p>
<ul>
<li>Login information to the platform such as AWS, Azure, Google Cloud, and in
our case, vSphere Server (ESXi). This is called the “provider”. Each provider
requires different information to create a VM or resource so we must read
Terraform doc for specifics on vSphere/ESXi.
<li>Data = data storage = hard drive capacity in ESXi. How to refer to them,
UUID or name? What is vSphere resource pool?
<li>Resource = VM or other Cloud resources such as S3, RDS, EC2. If VM, we
define the common requirements such as cpu, ram, disk space, operating system,
etc.
</li>
</ul>
<p>
4/9/20 - when trying to clone, error “error loading cluster: this operation is
only supported on vCenter” regarding vsphere_compute_cluster and vsphere_network
(both are required for the operation). Fix by defining an empty cluster.
</p>
<p>
4/15/20 - We successfully create a VM (shell, without OS) using Terraform. The
only wrinkle is that the script always returns with an error message.
</p>
<p>
The main.tf and variable.tf create a blank linux VM on our stark ESXi. They can
be run from your laptop or any remote VM which can access (ping) ESXi host
through OpenVPN. See the variable file for details specific to our environment.
The process still ends in a time-out error (1 min) because ESXi does not return
an ip address of the new VM. The root cause of this is that our newly created VM
is just a shell, no OS yet thus, it cannot be booted up.
</p>
<h2>ANSIBLE</h2>
<p>
Ansible is a configuration management (CM) tool. It allows you to write a yml
script (aka “a playbook”) to install software such as python and mysql onto an
existing host. You run the script with the command “ansible-playbook”. It looks
similar to docker-compose command (which also runs a yml script to create a
container).
</p>
<p>
In general, a typical script would:
</p>
<ul>
<li>Read the ansible.cfg file where you group your hosts/VMs (ex. [group1] ip1,
ip2…). When the script refers to a group, it will install to all of the hosts in
that group.
<li>Assume root/sysadmin role on the target host, install whatever you need. If
there is a dependency, you must install them in order. For example, you install
mysql, create database, user, then run sql command to create tables.
<li>Indentation is important in writing the script.
</li>
</ul>
<dl>
  <dt>can ansible install an OS to a blank VM?</dt>
</dl>
<p>
NOTE: Ansible can work with Kickstart. So, it’s possible to install an OS.
</p>
<h2>CONCLUSION</h2>
<p>
Each tool (Ansible, Terraform) is geared/optimized to handle different aspects
of automatic provisioning and managing VMs. As in an orchestra, the conductor is
like Terraform while the instrument tuner is like Ansible. Terraform can create
a VM and Ansible can configure, install software and applications onto that VM.
The “right way” or recommended best practice to create a Centos VM is for
Terraform to clone a VMDK template using vCenter API. Since we don’t have
vCenter, we will have to write a shell/Kickstart script to install the OS. This
is our plan for automating deployment.
</p>
<h2>REFERENCES</h2>
<p>
vSphere = ESXi + vCenter, etc. <a
href="https://www.mustbegeek.com/difference-between-vsphere-esxi-and-vcenter/">https://www.mustbegeek.com/difference-between-vsphere-esxi-and-vcenter/</a>
</p>
<p>
Ansible vs terraform = <a
href="https://linuxacademy.com/blog/linux-academy/ansible-vs-terraform-fight/">https://linuxacademy.com/blog/linux-academy/ansible-vs-terraform-fight/</a>
</p>
<p>
Learn terraform = <a
href="https://learn.hashicorp.com/terraform">https://learn.hashicorp.com/terraform</a>
</p>
<p>
<a
href="https://learn.hashicorp.com/terraform/getting-started/intro">https://learn.hashicorp.com/terraform/getting-started/intro</a>
</p>
<p>
3rd party tool = <a
href="https://github.com/josenk/terraform-provider-esxi">https://github.com/josenk/terraform-provider-esxi</a>
</p>
<p>
Terraform provisioner/pack = <a
href="https://www.terraform.io/docs/provisioners/index.html">https://www.terraform.io/docs/provisioners/index.html</a>
</p>
<p>
Terraform examples:
</p>
<p>
<a
href="https://vcloudvision.com/2019/04/12/provision-vsphere-vms-using-terraform/">https://vcloudvision.com/2019/04/12/provision-vsphere-vms-using-terraform/</a>
</p>
<p>
<a
href="https://buildvirtual.net/building-vsphere-virtual-machines-with-terraform/">https://buildvirtual.net/building-vsphere-virtual-machines-with-terraform/</a>
</p>
<p>
Deploy VM = <a
href="https://elatov.github.io/2018/07/use-terraform-to-deploy-a-vm-in-esxi/">https://elatov.github.io/2018/07/use-terraform-to-deploy-a-vm-in-esxi/</a>
</p>
<p>
ESXi VM & networking = <a
href="https://support.us.ovhcloud.com/hc/en-us/articles/360003263859-How-to-Create-a-VM-in-VMware-ESXi-6-5">https://support.us.ovhcloud.com/hc/en-us/articles/360003263859-How-to-Create-a-VM-in-VMware-ESXi-6-5</a>
</p>
<p>
vSphere provider = <a
href="https://www.terraform.io/docs/providers/vsphere/index.html">https://www.terraform.io/docs/providers/vsphere/index.html</a>
</p>
<p>
Ansible for beginner = <a
href="https://www.youtube.com/watch?v=dCQpaTTTv98">https://www.youtube.com/watch?v=dCQpaTTTv98</a>
</p>
<p>
Ansible and Kickstart = <a
href="https://medium.com/@jackprice/pushing-kickstarts-with-ansible-122b2cd9c1e">https://medium.com/@jackprice/pushing-kickstarts-with-ansible-122b2cd9c1e</a>
</p>
