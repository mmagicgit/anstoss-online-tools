import "./globals.css";
import React from "react";
import Nav from "@/components/Nav";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html>
      <body>
        <Nav></Nav>
        {children}
      </body>
    </html>
  );
}
