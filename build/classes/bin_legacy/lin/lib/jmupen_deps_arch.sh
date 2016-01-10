#!/bin/bash

if [ `whoami` != root ]; then
	echo "this script needs to be run as root.";
	exit 0;
fi

pacman -S --noconfirm sdl glu 

pacman -U --noconfirm libpng14.tar.xz
