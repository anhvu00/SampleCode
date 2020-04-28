# example from terraform.io
# Edited 4/15/20 Anh
# This creates a VM shell (no OS) on ESXi.
# Before run, please confirm all hard coded values such as
# user, password, datastore, datacenter, VM specs, etc.
# Also, change user/password in variables.tf file.
# Usage: On terminal type:
# 1. terraform plan
# 2. terraform apply

provider "vsphere" {
  user           = var.vsphere_user
  password       = var.vsphere_password
  vsphere_server = var.vsphere_server

  # If you have a self-signed cert
  allow_unverified_ssl = true

}

data "vsphere_datacenter" "dc" {
  name = "dc1"
}

data "vsphere_datastore" "datastore" {
  name = "ATA-48bc02c"
  datacenter_id = data.vsphere_datacenter.dc.id
}

data "vsphere_resource_pool" "pool" {
  name          = "cluster1/Resources"
  datacenter_id = data.vsphere_datacenter.dc.id
}

data "vsphere_network" "network" {
  name          = "VM Network"
  datacenter_id = data.vsphere_datacenter.dc.id
}

resource "vsphere_virtual_machine" "vm" {
  name             = "anh-terraform-test"
  resource_pool_id = data.vsphere_resource_pool.pool.id
  datastore_id     = data.vsphere_datastore.datastore.id

  num_cpus = 4
  memory   = 8192
  guest_id = "other3xLinux64Guest"

  wait_for_guest_net_timeout = 1

  network_interface {
    network_id = data.vsphere_network.network.id
  }

  disk {
    label = "disk0"
    size  = 80
  }
}

