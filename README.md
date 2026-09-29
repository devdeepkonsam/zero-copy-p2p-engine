# Zero-Copy Multi-Platform P2P Asset Streaming Engine

> **Final Year B.Tech CSE Capstone Project & Research Publication Blueprint**

---

## Table of Contents
1. [Ready-to-Copy Google Form Template (For 3-Member Team)](#1-ready-to-copy-google-form-template-for-3-member-team)
2. [Team Task Allocation (3 Members)](#2-team-task-allocation-3-members)
3. [Estimated Timeline Breakdown (Total: ~14 Weeks)](#3-estimated-timeline-breakdown-total-14-weeks)
4. [Complete Project Lifecycle & Phase Diagram](#4-complete-project-lifecycle--phase-diagram)
5. [System Architecture & Full Data Flow](#5-system-architecture--full-data-flow)
6. [System Execution Flowchart](#6-system-execution-flowchart)
7. [Step-by-Step Technical Execution Logic](#7-step-by-step-technical-execution-logic)
8. [Complete Project Monorepo File Structure](#8-complete-project-monorepo-file-structure)
9. [Prerequisites & Environment Configuration](#9-prerequisites--environment-configuration)
10. [Step-by-Step Installation & Quickstart Guide](#10-step-by-step-installation--quickstart-guide)
11. [WebSocket Signaling Protocol & API Contract](#11-websocket-signaling-protocol--api-contract)
12. [Benchmarking & Empirical Data Collection Guide](#12-benchmarking--empirical-data-collection-guide)
13. [Target Publication Venues & Journal Guidelines](#13-target-publication-venues--journal-guidelines)
14. [External Examiner Defense & Viva Preparation (FAQ)](#14-external-examiner-defense--viva-preparation-faq)
15. [References](#15-references)

---

## 1. Ready-to-Copy Google Form Template (For 3-Member Team)

```text
PROJECT SELECTION FORM DETAILS
================================================================================




Domain: 
Distributed Systems / Computer Networks / Operating Systems

Tech Stack: 
Java (NIO, FileChannel, Direct ByteBuffer), WebSockets, WebRTC, React, Tailwind CSS, Node.js

Problem Statement: 
Traditional client-server file distribution can create central server bandwidth bottlenecks when distributing large files to multiple clients. Browser-based P2P systems can also experience memory pressure if complete files or too many chunks are retained during transfer across heterogeneous desktop and mobile environments.

Proposed Methodology: 
We propose a hybrid peer-to-peer distribution engine that evaluates bounded Java NIO file reads using heap and direct buffers. A direct buffer allocates memory outside the normal JVM heap and may reduce intermediate user-space copies during native I/O, but it does not guarantee OS-level zero-copy. `FileChannel.transferTo()` may reduce copying between file and socket channels on supported combinations of operating system, filesystem, JVM, and channel types; it may still fall back to another implementation and may require multiple calls to transfer all bytes. A cloud-hosted coordinator handles peer signaling, while received chunks are checked with SHA-256 before incremental browser storage. A Merkle root can provide an additional whole-file integrity check when its construction and verification rules are agreed by both peers.

Measurable Research Metrics (For Journal/Conference Publication):
1. End-to-end throughput and completion time for 100 MB–500 MB MVP transfers and 1–5 GB stress tests.
2. JVM heap usage, direct/off-heap memory usage, CPU utilization, and garbage-collection behavior.
3. Comparison between simple buffered Java I/O and Java NIO with direct buffers; benchmark `transferTo()` separately where a compatible channel exists.

Team Member Roles & Task Division (3 Members):
- Member 1 (Team Leader): Java NIO Core Engine, Zero-Copy Direct Memory Allocation, and JVM Performance Profiling.
- Member 2: Node.js Signaling Server, WebSockets, WebRTC DataChannels, and Peer Routing Logic.
- Member 3: React Dashboard UI, Client-side Merkle Tree Chunk Verification, and IEEE Research Paper Drafting.
================================================================================
```

```text
                  ┌─────────────────────────────────────────┐
                  │              TEAM LEADER                │
                  │   Systems & Performance Lead (M1)       │
                  └────────────────────┬────────────────────┘
                                       │
            ┌──────────────────────────┴──────────────────────────┐
            ▼                                                     ▼
┌──────────────────────────────┐                       ┌──────────────────────────────┐
│  Networking & Protocol (M2)  │                       │   Frontend & Research (M3)   │
└──────────────────────────────┘                       └──────────────────────────────┘
```

---

## 2. Team Task Allocation (3 Members)

### Member 1: Systems & Performance Lead (Team Leader)
* **Core Technical Role:** Java NIO Core Engine & Memory Optimization.
* **Responsibilities:**
  * Build the Java NIO streaming pipeline using `FileChannel` and bounded `DirectByteBuffer` instances to evaluate sender-side memory and copying behavior.
  * Implement file slicing (1 MB–4 MB chunks) and reuse a bounded number of buffers.
  * Run VisualVM / JConsole profiling to collect JVM Heap Memory metrics during stress tests.

### Member 2: Networking & Peer Discovery Lead
* **Core Technical Role:** WebSockets Signaling & WebRTC P2P DataChannels.
* **Responsibilities:**
  * Build the Node.js signaling server to handle peer discovery and handshakes.
  * Implement WebRTC DataChannels for peer-to-peer byte streaming across devices.
  * Handle edge cases like connection drops, timeout retries, and NAT traversal fallback.

### Member 3: Frontend, Verification & Paper Lead
* **Core Technical Role:** Client Interface, Cryptographic Validation & Research Paper Drafting.
* **Responsibilities:**
  * Build the React / Tailwind CSS user interface (progress bars, speed meters, active peer counts).
  * Implement client-side Merkle Tree generation and SHA-256 chunk verification.
  * Manage the IEEE Overleaf LaTeX template, format benchmark graphs, and lead the research paper drafting.

---

## 3. Estimated Timeline Breakdown (Total: ~14 Weeks)

### Phase 1: Core System & Architecture Setup (Weeks 1 – 3)
* **Week 1 (Signaling & Protocol):** Build the Node.js signaling server using WebSockets. Establish the peer handshake mechanism between desktop browsers/clients and mobile devices.
* **Week 2 (Java NIO Engine):** Set up the Java backend. Write low-level `FileChannel` pipelines using bounded heap and direct buffers, then measure their memory and CPU behavior. Direct buffers are off-heap; they do not mean that the complete file bypasses all user-space memory.
* **Week 3 (Merkle Tree & Integrity):** Write the chunking logic (breaking files into 1 MB–4 MB chunks), implement SHA-256 hashing, and generate Merkle Trees for data validation.

### Phase 2: WebRTC Integration & Cross-Platform Client (Weeks 4 – 6)
* **Week 4 (P2P Data Channels):** Integrate WebRTC DataChannels on the frontend (React) to allow peer-to-peer chunk transfers between devices without hitting a central server.
* **Week 5 (Memory Optimization):** Connect the Java NIO engine with the frontend client. Tune direct buffers to avoid memory leaks during high-speed transfers.
* **Week 6 (Integration & UI):** Build the dashboard UI (progress indicators, peer counts, current throughput charts). Conduct end-to-end small file transfer tests.

### Phase 3: Benchmarking & Stress Testing for Journal (Weeks 7 – 9)
* **Week 7 (Stress Test Setup):** Set up a local test environment with heterogeneous devices (e.g., Windows laptop plus Android/iPhone on Wi-Fi or LAN). Prepare 100 MB–500 MB MVP files and 1–5 GB stress files; attempt 10 GB only if feasible.
* **Week 8 (Data Collection):** Measure and record throughput (MB/s), JVM heap memory utilization (using tools like JConsole/VisualVM), and CPU usage comparing:
  * Standard Java I/O (`FileInputStream` / `FileOutputStream`).
  * Java NIO Direct Memory (`FileChannel.transferTo`).
* **Week 9 (Plotting & Analysis):** Generate comparative performance charts (Throughput vs. File Size, Heap Footprint vs. Time, Latency Graphs).

### Phase 4: Paper Writing & Journal/Conference Submission (Weeks 10 – 12)
* **Week 10 (LaTeX Setup & Draft):** Set up the IEEE 2-column LaTeX template on Overleaf. Draft the Abstract, Introduction, System Architecture, and Algorithm Pseudocode.
* **Week 11 (Results & Related Work):** Add the benchmarking graphs, literature review (citing 10–15 recent papers in distributed systems), and mathematical throughput formulas.
* **Week 12 (Review & Upload):** Review the draft with your allocated faculty guide and submit the manuscript to a target Scopus-indexed journal or IEEE/Springer Conference.

---

## 4. Complete Project Lifecycle & Phase Diagram
```text
[PHASE 1: REGISTRATION & PLANNING]
  |-- Submit Selection Form with 3 Members (Same Section)
  |-- Finalize Project Title & Problem Statement
  |-- Faculty Guide Allocation & Initial Milestone Sign-off
  v
[PHASE 2: CORE DEVELOPMENT & MODULES]  (Weeks 1 - 6)
  |-- Member 1: Build Java NIO Engine (FileChannel, DirectByteBuffer, Merkle Tree)
  |-- Member 2: Build Node.js Signaling Server & WebRTC DataChannel Handshakes
  |-- Member 3: Build React Client, Dashboard UI & Client-side Chunk Assembler
  v
[PHASE 3: INTEGRATION & BENCHMARKING]  (Weeks 7 - 9)
  |-- Connect Java NIO Backend to WebRTC P2P Data Channels
  |-- Demonstrate 100 MB to 500 MB MVP Transfers
  |-- Run 1 GB to 5 GB Stress Tests; attempt 10 GB only if feasible
  |-- Capture Telemetry: Throughput (MB/s), Latency (ms), and Heap Memory (MB)
  v
[PHASE 4: RESEARCH PAPER & PUBLICATION]  (Weeks 10 - 12)
  |-- Draft IEEE 2-Column Conference/Journal Paper in Overleaf (LaTeX)
  |-- Plot Comparative Benchmarks (Buffered I/O vs. NIO Direct Buffers)
  |-- Submit Paper to Scopus-Indexed Journal or IEEE/Springer Conference
  v
[PHASE 5: FINAL EVALUATION & CAPSTONE VIVA]  (Semester End)
  |-- Present Live Working Prototype to External Examiners
  |-- Submit Journal Acceptance Letter / Published Paper Proof
```

---

## 5. System Architecture & Full Data Flow

```text
                  +--------------------------------------------------------------------------------------+
                  |                                MEMBER 2: CLOUD SIGNALING                             |
                  |                               (Node.js / WebSockets / STUN)                          |
                  +------------------------------------------+-------------------------------------------+
                                                             |
                                   1. WebSocket Handshake    |    2. WebSocket Handshake
                                   & Peer Session Register   |    & Swarm Discovery
                                                             v
            +------------------------------------------------+------------------------------------------------+
            |                                                                                                 |
            v                                                                                                 v
+---------------------------------------+                                                       +---------------------------------------+
|        MEMBER 1: SENDER NODE          |                                                       |       MEMBER 3: RECEIVER NODE         |
|        (Java NIO / OS Kernel)         |                                                       |        (React / WebRTC / Web)         |
+---------------------------------------+                                                       +---------------------------------------+
|                                       |                                                       |                                       |
| [Step 1: File Ingestion & Slicing]    |                                                       |                                       |
| - Select Large File (e.g., 10 GB)     |                                                       |                                       |
| - Memory-Map File via FileChannel     |                                                       |                                       |
| - Slice File into 2 MB - 4 MB Chunks  |                                                       |                                       |
|                                       |                                                       |                                       |
| [Step 2: Crypto Pre-Computation]      |                                                       |                                       |
| - Compute SHA-256 for each Chunk      |                                                       |                                       |
| - Construct Merkle Tree & Root Hash   |                                                       |                                       |
| - Generate File Manifest Metadata     |                                                       |                                       |
|                                       |                                                       |                                       |
|                                       |     3. ICE Candidates / SDP Offer-Answer Exchange     |                                       |
|                                       | <===================================================> |                                       |
|                                       |                                                       |                                       |
|                                       | ===================================================== |                                       |
|                                       |   DIRECT P2P WEBRTC DATACHANNEL / SOCKET ESTABLISHED  |                                       |
|                                       |   (Central Signaling Server is bypassed from here)    |                                       |
|                                       | ===================================================== |                                       |
|                                       |                                                       |                                       |
|                                       | <---------------------------------------------------- | [Step 3: Initial Handshake]           |
|                                       |           4. Request Manifest & Merkle Root           | - Query Available Swarm               |
|                                       |                                                       | - Download Metadata & Root Hash       |
|                                       |                                                       |                                       |
|                                       |                                                       | [Step 4: Chunk Request Loop]          |
|                                       | <---------------------------------------------------- | - Request Chunk #N Index              |
|                                       |                 5. Request Chunk #N                   |                                       |
|                                       |                                                       |                                       |
| [Step 5: Zero-Copy Execution Engine]  |                                                       |                                       |
| - Read Chunk via DirectByteBuffer     |                                                       |                                       |
| - Bypass JVM Heap Space               |                                                       |                                       |
|   (Disk -> OS Cache -> Socket Buffer) |                                                       |                                       |
|                                       |                                                       |                                       |
|                                       | ----------------------------------------------------> | [Step 6: Stream Ingestion]            |
|                                       |            6. Stream Raw Binary Chunk #N              | - Receive ArrayBuffer via WebRTC      |
|                                       |                                                       |                                       |
|                                       |                                                       | [Step 7: Integrity & Assembly]        |
|                                       |                                                       | - Compute SHA-256 on Received Chunk   |
|                                       |                                                       | - Verify Against Merkle Tree Branch   |
|                                       |                                                       | - Write Chunk to Local File System    |
|                                       |                                                       |                                       |
|                                       | <---------------------------------------------------- | [Step 8: Feedback & Continuation]     |
|                                       |              7. Send Chunk ACK / Next Request         | - Update Progress Dashboard (MB/s)    |
|                                       |                                                       | - Request Next Missing Chunk          |
|                                       |                                                       |                                       |
+---------------------------------------+                                                       +---------------------------------------+
                    |                                                                                               |
                    |                                                                                               |
                    +----------------------------------------------+------------------------------------------------+
                                                                   |
                                                                   v
                                        +-----------------------------------------------------+
                                        |            MEMBER 1, 2 & 3: BENCHMARKING            |
                                        +-----------------------------------------------------+
                                        | - Capture JVM Heap Footprint (VisualVM / JConsole)  |
                                        | - Measure Throughput (MB/s) vs. File Size (1-10 GB) |
                                        | - Record Latency Metrics for IEEE Paper             |
                                        +-----------------------------------------------------+
```

---

## 6. System Execution Flowchart

```text
┌────────────────────────┐      ┌────────────────────────┐      ┌────────────────────────┐
│  MEMBER 2: SIGNALING   │      │   MEMBER 1: SENDER     │      │  MEMBER 3: RECEIVER    │
│  (Node.js / WebSockets)│      │  (Java NIO / Desktop)  │      │   (React / Mobile)     │
└───────────┬────────────┘      └───────────┬────────────┘      └───────────┬────────────┘
            │                               │                               │
            │  1. Connect WebSocket         │                               │
            │◄──────────────────────────────┤                               │
            │                               │                               │
            │  2. Connect WebSocket         │                               │
            │◄──────────────────────────────────────────────────────────────┤
            │                               │                               │
            │                               │  3. Select 10GB+ File         │
            │                               │─────┐                         │
            │                               │     │ Java NIO FileChannel    │
            │                               │     │ Slices into 2MB Chunks  │
            │                               │◄────┘                         │
            │                               │                               │
            │                               │  4. Compute Merkle Tree       │
            │                               │─────┐                         │
            │                               │     │ Generate SHA-256 Hashes│
            │                               │◄────┘                         │
            │                               │                               │
            │  5. Register File Hash        │                               │
            │◄──────────────────────────────┤                               │
            │                               │                               │
            │                               │  6. Query Available Swarm     │
            │                               │◄──────────────────────────────┤
            │  7. Send WebRTC Offer/Answer  │                               │
            │◄──────────────────────────────┼───────────────────────────────┤
            │   (STUN/ICE Candidates)       │                               │
            │                               │                               │
            =================================================================
             DIRECT P2P WEBRTC DATACHANNEL ESTABLISHED (BYPASSES SIGNALING)
            =================================================================
            │                               │                               │
            │                               │  8. Request Chunk #0 Manifest │
            │                               │◄──────────────────────────────┤
            │                               │                               │
            │                               │  9. Zero-Copy Kernel Transfer │
            │                               │─────┐                         │
            │                               │     │ DirectByteBuffer        │
            │                               │     │ Bypasses JVM Heap RAM   │
            │                               │◄────┘                         │
            │                               │                               │
            │                               │  10. Stream Raw Binary Chunk  │
            │                               ├──────────────────────────────►│
            │                               │                               │
            │                               │                               │ 11. Verify Chunk Hash
            │                               │                               │─────┐
            │                               │                               │     │ Validate Merkle Tree
            │                               │                               │     │ Write to Storage
            │                               │◄────┘                         │
            │                               │                               │
            │                               │  12. Send Chunk ACK           │
            │                               │◄──────────────────────────────┤
            │                               │                               │
            │                               │ (Loop steps 8-12 for all)     │
            │                               │                               │
```

---

## 7. Step-by-Step Technical Execution Logic

### Phase A: Peer Discovery & Signaling (Member 2's Scope)
* **Handshake:** Both the Sender (Java Desktop Node) and Receiver (React Web Browser) establish a persistent WebSocket connection with the Node.js Signaling Server hosted in the cloud.
* **WebRTC Negotiation:** The signaling server exchanges SDP (Session Description Protocol) offers/answers and ICE Candidates between the peers to punch through local NAT/firewalls.

### Phase B: Slicing & Zero-Copy Reading (Member 1's Scope)
* **OS Kernel Channel Mapping:** On the Sender side, Java's `FileChannel.open()` creates a direct memory-mapped file descriptor to the disk.
* **Zero-Copy Pipeline:** Instead of copying raw bytes into the JVM Heap (which triggers Garbage Collection freezes on large 10 GB files), the system uses `DirectByteBuffer` or `FileChannel.transferTo()`. Bytes travel directly from:
  $$\text{Hard Drive Storage} \longrightarrow \text{OS Page Cache} \longrightarrow \text{Network Socket Buffer} \longrightarrow \text{Network Interface Card (NIC)}$$
* **Merkle Tree Pre-computation:** The sender hashes each 2 MB chunk using SHA-256 and constructs a Merkle Tree to ensure data integrity during transmission.

### Phase C: Stream Transport & Verification (Member 3's Scope)
* **DataChannel Streaming:** Once the WebRTC peer connection is active, the Signaling Server steps back. Raw binary chunks travel directly Peer-to-Peer over WebRTC/SCTP or raw TCP Sockets.
* **Client-Side Integrity Verification:** The Receiver (Member 3's React client) receives the binary `ArrayBuffer`, re-hashes the chunk, and verifies it against the Merkle Root hash.
* **Assembly:** If valid, the chunk is appended directly to browser storage (`FileSystemWritableFileStream` or `IndexedDB`) and an Acknowledgement (ACK) is sent back to request the next chunk.

---

## 8. Complete Project Monorepo File Structure

```text
zero-copy-p2p-engine/
│
├── .gitignore
├── README.md
├── docker-compose.yml
│
├── backend-engine/                 # MEMBER 1: Java NIO Engine
│   ├── pom.xml                     # Maven configuration
│   └── src/
│       ├── main/
│       │   └── java/
│       │       └── com/p2p/engine/
│       │           ├── Main.java
│       │           ├── core/
│       │           │   ├── ZeroCopyChannel.java     # DirectByteBuffer & FileChannel logic
│       │           │   ├── ChunkManager.java        # Slicing & chunk streaming
│       │           │   └── MemoryProfiler.java      # JConsole/VisualVM telemetry hook
│       │           ├── crypto/
│       │           │   ├── MerkleTree.java          # Merkle tree generation
│       │           │   └── HashValidator.java       # SHA-256 hash checks
│       │           └── network/
│       │               ├── SocketServer.java        # TCP/WebSocket byte transport
│       │               └── PeerBridge.java          # Bridge to WebRTC data pipe
│       └── test/
│           └── java/com/p2p/engine/
│               ├── ZeroCopyBenchmarkTest.java       # Stress test suite (1GB-10GB)
│               └── MerkleTreeTest.java
│
├── signaling-server/               # MEMBER 2: Node.js Signaling Coordinator
│   ├── package.json
│   ├── server.js                   # WebSocket & HTTP server entry
│   └── src/
│       ├── controllers/
│       │   ├── sessionController.js # Room and swarm management
│       │   └── sdpHandler.js        # WebRTC Offer/Answer & ICE exchange
│       └── utils/
│           └── logger.js            # Connection and handshake logging
│
├── frontend-client/                # MEMBER 3: React Client Application
│   ├── package.json
│   ├── tailwind.config.js
│   ├── vite.config.js
│   ├── public/
│   └── src/
│       ├── App.jsx
│       ├── main.jsx
│       ├── components/
│       │   ├── Dashboard.jsx        # Transfer speed (MB/s) & active peer counts
│       │   ├── FileSelector.jsx     # Upload / download triggers
│       │   ├── MerkleViewer.jsx     # Visual Merkle verification tree
│       │   └── ProgressBar.jsx      # Chunk download progress meter
│       ├── hooks/
│       │   ├── useWebRTC.js         # Peer connection & DataChannel hook
│       │   └── useSignaling.js      # WebSocket signaling connector
│       └── utils/
│           ├── chunkVerifier.js     # Client SHA-256 chunk hash verifier
│           └── fileAssembler.js     # FileSystemWritableStream disk writer
│
├── benchmarks/                     # SHARED: Performance Data for Research Paper
│   ├── datasets/                    # Dummy test payload generators
│   ├── logs/                        # Raw CSV logs of RAM, CPU, Throughput
│   ├── scripts/
│   │   ├── plot_graphs.py           # Matplotlib script to generate IEEE charts
│   │   └── memory_monitor.sh        # System memory footprint logger
│   └── results/                     # Generated PNG/EPS charts for Overleaf
│
└── research-paper/                 # MEMBER 3 & ALL: IEEE Publication Materials
    ├── IEEE_Conference_Template.tex # Overleaf LaTeX main file
    ├── references.bib               # 15+ Scopus/IEEE citations
    └── figures/                     # Architecture diagrams and benchmark graphs
```

## 9. Prerequisites & Environment Configuration

### Software Requirements
| Tool / Runtime | Minimum Version | Recommended Version | Purpose |
| :--- | :--- | :--- | :--- |
| **Java Development Kit (JDK)** | OpenJDK 17 LTS | OpenJDK 21 LTS | Java NIO Engine & Kernel Channels |
| **Node.js** | v18.16.0 LTS | v20.11.0 LTS | Signaling Coordinator Server |
| **Build Tool (Java)** | Apache Maven 3.8+ | Maven 3.9+ | Backend dependency management |
| **Package Manager (JS)** | npm 9+ | npm 10+ | Frontend and Signaling dependencies |
| **Python** | 3.9+ | 3.11+ | Benchmark data plotting (Matplotlib) |

### Tested Operating Systems
* **Desktop Nodes:** Windows 11 / Ubuntu 22.04 LTS / macOS Sonoma
* **Mobile Nodes:** iOS 16+ (Safari WebRTC) / Android 12+ (Chrome WebRTC)

---

## 10. Step-by-Step Installation & Quickstart Guide

### Step 1: Clone Repository & Setup
```bash
git clone https://github.com/your-org/zero-copy-p2p-engine.git
cd zero-copy-p2p-engine
```

### Step 2: Start Signaling Server (Member 2's Module)
```bash
cd signaling-server
npm install
npm run dev
# Expected output: Signaling Server listening on ws://localhost:8080
```

### Step 3: Run Java NIO Engine (Member 1's Module)
```bash
cd ../backend-engine
mvn clean compile
mvn exec:java -Dexec.mainClass="com.p2p.engine.Main"
# Expected output: Direct Memory Pool allocated. Listening on TCP 9090
```

### Step 4: Run Frontend Client (Member 3's Module)
```bash
cd ../frontend-client
npm install
npm run dev
# Expected output: Local client running at http://localhost:5173
```

---

## 11. WebSocket Signaling Protocol & API Contract

### JSON Message Specification

#### 1. Peer Swarm Registration (CLIENT -> SERVER)
```json
{
  "event": "REGISTER_PEER",
  "peerId": "peer-node-9821-uuid",
  "fileRootHash": "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
  "role": "SEEDER"
}
```

#### 2. WebRTC Session Description (CLIENT <-> PEER via SERVER)
```json
{
  "event": "SDP_SIGNAL",
  "senderPeerId": "peer-node-9821-uuid",
  "targetPeerId": "peer-node-4311-uuid",
  "type": "OFFER",
  "sdp": "v=0\r\no=- 46117314004311890 2 IN IP4 127.0.0.1..."
}
```

#### 3. Binary Chunk Transfer Request (RECEIVER -> SENDER via DataChannel)
```json
{
  "event": "REQUEST_CHUNK",
  "chunkIndex": 42,
  "chunkSizeBytes": 2097152,
  "expectedChunkHash": "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"
}
```

#### 4. Chunk Verification Acknowledgement (RECEIVER -> SENDER)
```json
{
  "event": "CHUNK_ACK",
  "chunkIndex": 42,
  "status": "VERIFIED_SUCCESS",
  "nextChunkIndex": 43
}
```

---

## 12. Benchmarking & Empirical Data Collection Guide

To collect publishable research data for your paper, execute these steps across the MVP range of 100 MB–500 MB, followed by stress tests using 1 GB–5 GB payloads. A 10 GB test should be attempted only if the available hardware, storage, and schedule make it feasible.

### 1. Generate Test Payloads

**On Windows (PowerShell):**
```powershell
fsutil file createnew test_payload_5GB.bin 5368709120
```

**On Linux / macOS:**
```bash
head -c 5G </dev/urandom > test_payload_5GB.bin
```

### 2. Capture Real-Time Memory Telemetry (Member 1)

Run `jstat` or VisualVM while streaming to verify Zero-Copy memory efficiency:
```bash
# Monitor JVM Heap Allocation every 1000ms
jstat -gcutil <PID_OF_JAVA_PROCESS> 1000
```

> [!NOTE]
> **Hypothesis to test:** A bounded-buffer design should keep live JVM heap usage approximately independent of total file size, provided completed chunks are not retained. Direct-buffer usage, heap usage, CPU, and throughput must all be measured; neither direct buffers nor `transferTo()` guarantee an OS-level zero-copy path on every platform.

### 3. Generate IEEE Publication Plots (Member 3)
```bash
cd benchmarks/scripts
python plot_graphs.py --input-logs ../logs/run_results.csv --output ../results/
```

---

## 13. Target Publication Venues & Journal Guidelines

### Paper Focus, Methodology / Approach, and Key Limitation

The paper positions this project as a practical comparison of sender-side I/O strategies within a browser-compatible P2P transfer system. It does not claim that Java `DirectByteBuffer` creates an end-to-end zero-copy path to a browser. The comparison is summarized below.

| Paper Focus | Methodology / Approach | Key Limitation |
| :--- | :--- | :--- |
| **High-Throughput Zero-Copy I/O** | Leverages OS kernel mechanisms, such as `sendfile()` or `splice()`, and may benefit from DMA-assisted transfers to reduce or eliminate user-space copies between file and socket buffers. These mechanisms are typically exposed through native APIs or system calls in C/C++ servers. | Implemented primarily through native code and OS-specific APIs; not directly usable from sandboxed browsers and not designed specifically for heterogeneous P2P swarms with web and mobile clients. |
| **Java NIO & Memory Management** | Benchmarks off-heap `DirectByteBuffer` and `FileChannel`, including `transferTo()` where a compatible channel is available, against standard heap-based Java I/O streams. The measurements cover throughput, heap and direct-memory usage, CPU utilization, and garbage-collection behavior. | Evaluations are commonly single-node, server-side file or socket I/O experiments. They do not by themselves address distributed P2P swarming, cross-platform clients, or browser integration. |
| **WebRTC Browser P2P Systems** | Uses WebRTC DataChannels, which carry SCTP over DTLS/UDP, and WebSockets for signaling to enable browser-compatible peer-to-peer file sharing without plugins. | Browser implementations expose received data as JavaScript-accessible `ArrayBuffer` or `Blob` values. They can experience memory pressure for multi-gigabyte transfers and cannot directly access OS-level zero-copy paths, Java `FileChannel` instances, or file descriptors. |

In this project, the zero-copy-related evaluation is primarily on the Java desktop sender. The browser receiver uses standard WebRTC and Web Storage/File System APIs. The MVP targets one Java sender, one or more browser or mobile receivers, and one signaling server. It implements chunk hashes and final Merkle-root verification; full multi-peer Merkle proofs and swarm scheduling are future extensions.

Since one research publication is mandatory, focus on these vetted venues:

### Recommended Conferences (Fast-track Review & Fixed Schedules)
* **IEEE International Conference on Distributed Computing Systems (ICDCS Workshops)**
* **IEEE International Conference on Advanced Networks and Telecommunications Systems (ANTS)**
* **Springer International Conference on Data Communication and Networks (ICDCN)**

### Recommended Scopus-Indexed Journals
* **Journal of Network and Computer Applications (Elsevier)** – High Impact
* **International Journal of Communication Systems (Wiley)**
* **PeerJ Computer Science** – Open Access / Fast Turnaround

---

## 14. External Examiner Defense & Viva Preparation (FAQ)

### Q1: Why use Java NIO DirectByteBuffer over standard byte arrays?
* **Answer:** Standard byte arrays are allocated on the managed JVM Heap. When reading large files, the Java Garbage Collector (GC) experiences severe "Stop-the-World" latency spikes while cleaning memory. `DirectByteBuffer` allocates native memory outside the JVM garbage-collected heap, enabling the operating system kernel to perform direct DMA (Direct Memory Access) transfers from disk to the network socket without copying memory into user space.

### Q2: How does the system handle lost or corrupted chunks during P2P transit?
* **Answer:** Each file is represented by a Merkle Tree where each leaf is the SHA-256 hash of a 2 MB chunk. When the receiver gets a chunk over WebRTC, it hashes the payload and validates the branch path against the root hash. If a mismatch occurs, the chunk is flagged as **CORRUPTED**, rejected, and re-requested from a different active peer in the swarm.

### Q3: Why is a Node.js signaling server used alongside a Java backend?
* **Answer:** Node.js utilizes an event-driven, non-blocking I/O model that is lightweight and ideal for managing thousands of concurrent WebSocket client handshakes and WebRTC SDP session negotiations. Java is reserved strictly for high-throughput, raw I/O-intensive byte manipulation and zero-copy disk streaming.


# Member 1 (You / Leader):
git checkout -b feature/backend-nio-engine

# Member 2:
git checkout -b feature/signaling-server

# Member 3:
git checkout -b feature/frontend-client
---

## Implementation Notes & Realistic Expectations

This project is designed as a bounded-buffer, chunked P2P streaming system with browser-based clients and a Java NIO desktop sender. The following notes define what “working correctly” means in practice and what is intentionally kept simple for a B.Tech capstone.

### Scope of Zero-Copy

- On the desktop sender, Java NIO with `DirectByteBuffer` and `FileChannel.transferTo()` is used to reduce heap allocations and, where supported by the operating system, filesystem, JVM, and channel types, reduce copying between file and socket channels.
- These APIs do not guarantee an OS-level zero-copy path in every environment. `FileChannel.transferTo()` may also transfer fewer bytes than requested and therefore may require multiple calls.
- This optimization does not extend into the browser. WebRTC delivers data to JavaScript as `ArrayBuffer` or `Blob`, which are handled in browser-managed memory.

### Memory Usage

- The sender reads and sends one chunk at a time, or uses a small bounded window, while reusing a limited pool of buffers.
- The receiver verifies each chunk with SHA-256 and writes it to storage, such as IndexedDB or the File System Access API, before releasing the chunk from memory.
- With this design, live memory usage should remain approximately bounded regardless of total file size, assuming completed chunks are not retained unnecessarily.

### MVP File Sizes

- **Demo target:** 100–500 MB transfers.
- **Stress-test target:** 1–5 GB transfers on stable networks and supported devices.
- **Optional:** Files larger than 5 GB, including 10 GB, only if network stability, storage capacity, device resources, and implementation maturity permit.

### Protocol Simplicity

- Chunks are requested, sent, and acknowledged over WebRTC DataChannels using a small application protocol, such as `REQUEST_CHUNK`, `CHUNK_DATA`, and `CHUNK_ACK`.
- Failed or corrupted chunks are retried after a SHA-256 mismatch or transfer error.
- The initial implementation assumes one sender and one or more receivers. Full multi-source swarming and dynamic peer scheduling are outside the MVP scope.

### Merkle-Tree Integrity

- Each file is divided into fixed-size chunks, and a SHA-256 hash is computed for every chunk.
- A Merkle tree may be built over these chunk hashes. Its root, together with the agreed tree-construction rules, can be shared with receivers through a trusted control path.
- The MVP verifies each received chunk against its expected SHA-256 hash. It does not imply full multi-peer Merkle-proof verification unless that feature is implemented explicitly.
- When Merkle-proof verification is implemented, a receiver can verify an individual chunk against a trusted Merkle root without receiving the complete leaf list.

### Network Considerations

- A cloud-hosted signaling server using Node.js and WebSockets coordinates peer discovery and WebRTC negotiation. It remains on the control path and does not carry application file chunks.
- Direct peer-to-peer connectivity is preferred after signaling. WebRTC may use a TURN relay when NAT or firewall restrictions prevent a direct route.
- Direct P2P and TURN-relayed transfers should be measured and reported separately because their throughput and latency characteristics may differ.



@misc{harvard_zerocopy_2008,
  author       = {{Harvard DCE CSCIE28}},
  title        = {Efficient data transfer through zero copy},
  howpublished = {Harvard University, Data Communications and Distributed Systems (CSCIE28) Technical Article},
  year         = {2008},
  url          = {https://cscie28.dce.harvard.edu/lectures/lect02/6_Extras/zero-copy/}
}

@inproceedings{ournani2021evaluating,
  author    = {Ournani, Zakaria and Rouvoy, Romain and Durieux, Thomas and Monperrus, Martin},
  title     = {Evaluating the Energy Consumption of Java I/O APIs},
  booktitle = {International Conference on Advanced Information Systems and Technologies (A-IST)},
  year      = {2021},
  url       = {https://discovery.researcher.life/article/evaluating-the-energy-consumption-of-java-i-o-apis/7cdbf0a13d2a36aca72b036275b13bd3}
}

@inproceedings{eskola2015webrtc,
  author    = {Eskola, Rasmus and Nurminen, Jukka K.},
  title     = {Performance evaluation of WebRTC data channels},
  booktitle = {2015 IEEE Symposium on Computers and Communication (ISCC)},
  year      = {2015},
  pages     = {676--680},
  doi       = {10.1109/ISCC.2015.7884873}
}

@inproceedings{merkle1980protocols,
  author    = {Merkle, Ralph C.},
  title     = {Protocols for Public Key Cryptosystems},
  booktitle = {IEEE Symposium on Security and Privacy},
  year      = {1980},
  pages     = {122--134},
  url       = {https://www.ralphmerkle.com/papers/Protocols.pdf}
}

@article{quad_merkle_blockchain_2023,
  title     = {Data Integrity Audit Scheme Based on Quad Merkle Tree and Blockchain},
  journal   = {ResearchGate Publication},
  year      = {2023},
  url       = {https://www.researchgate.net/publication/367440108_Data_Integrity_Audit_Scheme_Based_on_Quad_Merkle_Tree_and_Blockchain}
}
