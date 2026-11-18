
import { useEffect, useState } from "react";
import "./App.css";
import {
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  PieChart,
  Pie,
  Cell
} from "recharts";

const API_URL = "http://localhost:8080/api";

function App() {
  const [telemetry, setTelemetry] = useState([]);
  const [queue, setQueue] = useState([]);
  const [activePage, setActivePage] = useState("Overview");
  const [history, setHistory] = useState([]);
  const [formData, setFormData] = useState({
  satelliteId: "",
  batteryLevel: "",
  linkQuality: "",
  dataType: "SENSOR",
  urgency: "MEDIUM"
  });
  const [queueForm, setQueueForm] = useState({
  satelliteId: "",
  dataType: "SENSOR",
  urgency: "MEDIUM",
  batteryLevel: "",
  linkQuality: ""
  });

  const [queueSubmitting, setQueueSubmitting] = useState(false);

  const [submitting, setSubmitting] = useState(false);

  const [loading, setLoading] = useState(true);
  const [queueLoading, setQueueLoading] = useState(false);
  const [transmittingId, setTransmittingId] = useState(null);

  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  // Fetch telemetry records
  const fetchTelemetry = async () => {
    try {
      const response = await fetch(`${API_URL}/telemetry`);

      if (!response.ok) {
        throw new Error("Unable to retrieve telemetry.");
      }

      const data = await response.json();
      setTelemetry(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  // Fetch priority queue
  const fetchQueue = async () => {
    setQueueLoading(true);

    try {
      const response = await fetch(`${API_URL}/queue`);

      if (!response.ok) {
        throw new Error("Unable to retrieve priority queue.");
      }

      const data = await response.json();
      setQueue(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message);
    } finally {
      setQueueLoading(false);
    }
  };

  useEffect(() => {
  fetchTelemetry();
  fetchQueue();
  fetchHistory();

  const intervalId = setInterval(() => {
    fetchTelemetry();
    fetchQueue();
    fetchHistory();
  }, 10000);

  return () => clearInterval(intervalId);
}, []);

// Fetch transmission history
const fetchHistory = async () => {
  try {
    const response = await fetch(
      `${API_URL}/transmission/history`
    );

    if (!response.ok) {
      throw new Error("Unable to retrieve transmission history.");
    }

    const data = await response.json();
    setHistory(Array.isArray(data) ? data : []);

  } catch (err) {
    setError(err.message);
  }
};

const handleTelemetrySubmit = async (event) => {
  event.preventDefault();

  setError("");
  setMessage("");
  setSubmitting(true);

  try {
    const payload = {
      satelliteId: formData.satelliteId.trim(),
      batteryLevel: Number(formData.batteryLevel),
      linkQuality: Number(formData.linkQuality),
      dataType: formData.dataType,
      urgency: formData.urgency
    };

    const response = await fetch(`${API_URL}/telemetry`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Unable to create telemetry record.");
    }

    await response.json();

    setMessage("Telemetry record added successfully!");

    setFormData({
      satelliteId: "",
      batteryLevel: "",
      linkQuality: "",
      dataType: "SENSOR",
      urgency: "MEDIUM"
    });

    await fetchTelemetry();

  } catch (err) {
    setError(err.message);
  } finally {
    setSubmitting(false);
  }
};

const handleQueueSubmit = async (event) => {
  event.preventDefault();

  setError("");
  setMessage("");
  setQueueSubmitting(true);

  try {
    const payload = {
      satelliteId: queueForm.satelliteId.trim(),
      dataType: queueForm.dataType,
      urgency: queueForm.urgency,
      batteryLevel: Number(queueForm.batteryLevel),
      linkQuality: Number(queueForm.linkQuality)
    };

    const response = await fetch(`${API_URL}/queue`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "Unable to add queue item.");
    }

    const savedItem = await response.json();

    setMessage(
      `Queue item added successfully! Priority: ${savedItem.priority}, Score: ${savedItem.priorityScore}`
    );

    setQueueForm({
      satelliteId: "",
      dataType: "SENSOR",
      urgency: "MEDIUM",
      batteryLevel: "",
      linkQuality: ""
    });

    await fetchQueue();

  } catch (err) {
    setError(err.message);
  } finally {
    setQueueSubmitting(false);
  }
};

const handleQueueFormChange = (event) => {
  const { name, value } = event.target;

  setQueueForm((previous) => ({
    ...previous,
    [name]: value
  }));
};

const handleFormChange = (event) => {
  const { name, value } = event.target;

  setFormData((previous) => ({
    ...previous,
    [name]: value
  }));
};

  // Simulated transmission
  const transmitItem = async (id) => {
    setMessage("");
    setError("");
    setTransmittingId(id);

    try {
      const response = await fetch(
        `${API_URL}/transmission/${id}`,
        { method: "POST" }
      );

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Transmission failed.");
      }

      await response.json();

      setMessage("Transmission simulated successfully.");
      await fetchQueue();
      await fetchHistory();
    } catch (err) {
      setError(err.message);
    } finally {
      setTransmittingId(null);
    }
  };

  // Dashboard statistics
  const totalSatellites = new Set(
    telemetry.map((item) => item.satelliteId)
  ).size;

  const averageBattery = telemetry.length
    ? (
        telemetry.reduce(
          (total, item) => total + Number(item.batteryLevel || 0),
          0
        ) / telemetry.length
      ).toFixed(1)
    : "0";

  const averageLinkQuality = telemetry.length
    ? (
        telemetry.reduce(
          (total, item) => total + Number(item.linkQuality || 0),
          0
        ) / telemetry.length
      ).toFixed(1)
    : "0";

  const getPriorityClass = (priority) => {
    const value = (priority || "UNKNOWN").toLowerCase();
    return `priority priority-${value}`;
  };

  const getStatusClass = (status) => {
    const value = (status || "UNKNOWN").toLowerCase();
    return `status status-${value}`;
  };

  const refreshAll = () => {
    setError("");
    setMessage("");
    fetchTelemetry();
    fetchQueue();
    fetchHistory();
  };

// Battery and link-quality chart data
const telemetryChartData = telemetry.map((item) => ({
  satellite: item.satelliteId,
  battery: Number(item.batteryLevel || 0),
  link: Number(item.linkQuality || 0)
}));

// Priority distribution
const priorityTypes = [
  "CRITICAL",
  "HIGH",
  "MEDIUM",
  "LOW",
  "HOLD"
];

const priorityChartData = priorityTypes.map((priority) => ({
  priority,
  count: queue.filter(
    (item) => item.priority === priority
  ).length
}));

// Priority chart colors
const priorityColors = {
  CRITICAL: "#dc2626",
  HIGH: "#f97316",
  MEDIUM: "#eab308",
  LOW: "#16a34a",
  HOLD: "#64748b"
};


  return (
    <div className="app-container">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-icon">🛰️</div>
          <div>
            <h2>OrbitLink</h2>
            <span>Satellite Control</span>
          </div>
        </div>

        <div className="nav-section">
          <p className="nav-heading">MAIN MENU</p>

          {[
            "Overview",
            "Telemetry",
            "Add Telemetry",
            "Add Queue Item",
            "Priority Queue",
            "Transmission History"
          ].map((page) => (
            <button
              key={page}
              className={`nav-button ${
                activePage === page ? "active" : ""
              }`}
              onClick={() => {
                setActivePage(page);
                setError("");
                setMessage("");
                if (page === "Add Telemetry") setFormData({
                  satelliteId: "",
                  batteryLevel: "",
                  linkQuality: "",
                  dataType: "SENSOR",
                  urgency: "MEDIUM"
                });
                if (page === "Add Queue Item") {
                  setQueueForm({
                    satelliteId: "",
                    dataType: "SENSOR",
                    urgency: "MEDIUM",
                    batteryLevel: "",
                    linkQuality: ""
                  });
                }
                if (page === "Priority Queue") fetchQueue();
                if (page === "Telemetry") fetchTelemetry();
                if (page === "Transmission History") fetchHistory();
              }}
            >
              <span>
                {page === "Overview"
                  ? "▦"
                  : page === "Telemetry"
                    ? "◉"
                    : page === "Add Telemetry"
                      ? "+"
                      : page === "Add Queue Item"
                        ? "＋"
                        : page === "Priority Queue"
                          ? "☷"
                          : "◷"}
              </span>
              {page}
            </button>
          ))}
        </div>

        <div className="sidebar-footer">
          <span className="connection-dot"></span>
          Backend connection
          <strong>API configured</strong>
        </div>
      </aside>

      <main className="main-content">
        <header className="topbar">
          <div>
            <h1>{activePage}</h1>
            <p>Satellite Telemetry and Data-Priority Dashboard</p>
          </div>

          <button className="refresh-button" onClick={refreshAll}>
            ↻ Refresh
          </button>
        </header>

        {error && (
          <div className="error-message">
            {error}
            <button onClick={refreshAll}>Retry</button>
          </div>
        )}

        {message && (
          <div className="success-message">
            ✓ {message}
          </div>
        )}

        {activePage === "Overview" && (
          <>
            {loading ? (
              <div className="loading-message">
                Loading satellite telemetry...
              </div>
            ) : (
              <>
                <section className="welcome-section">
                  <div>
                    <h2>Mission Overview</h2>
                    <p>
                      Monitor satellite health and communication performance.
                    </p>
                  </div>
                  <span className="live-label">
                    <span className="connection-dot"></span>
                    LIVE DATA
                  </span>
                </section>

                <section className="stats-grid">
                  <div className="stat-card">
                    <div className="stat-icon">🛰️</div>
                    <p>Total Satellites</p>
                    <h2>{totalSatellites}</h2>
                    <span>Unique satellite IDs</span>
                  </div>

                  <div className="stat-card">
                    <div className="stat-icon">🔋</div>
                    <p>Average Battery</p>
                    <h2>{averageBattery}%</h2>
                    <span>Across telemetry records</span>
                  </div>

                  <div className="stat-card">
                    <div className="stat-icon">📡</div>
                    <p>Average Link Quality</p>
                    <h2>{averageLinkQuality}%</h2>
                    <span>Communication quality</span>
                  </div>

                  <div className="stat-card">
                    <div className="stat-icon">📊</div>
                    <p>Queued Data Items</p>
                    <h2>{queue.length}</h2>
                    <span>Items awaiting review</span>
                  </div>
                </section>

                {/* DASHBOARD ANALYTICS */}

                <section className="charts-grid">

                  {/* BATTERY AND LINK QUALITY */}

                  <div className="chart-card">
                    <div className="chart-heading">
                      <h2>Satellite Health</h2>
                      <p>Battery and communication link quality</p>
                    </div>

                    {telemetryChartData.length === 0 ? (
                      <div className="empty-message">
                        No telemetry data available.
                      </div>
                    ) : (
                      <ResponsiveContainer width="100%" height={280}>
                        <BarChart data={telemetryChartData}>
                          <CartesianGrid strokeDasharray="3 3" />
                          <XAxis dataKey="satellite" />
                          <YAxis domain={[0, 100]} />
                          <Tooltip />
                          <Legend />

                          <Bar
                            dataKey="battery"
                            name="Battery (%)"
                            fill="#2563eb"
                            radius={[5, 5, 0, 0]}
                          />

                          <Bar
                            dataKey="link"
                            name="Link Quality (%)"
                            fill="#16a34a"
                            radius={[5, 5, 0, 0]}
                          />
                        </BarChart>
                      </ResponsiveContainer>
                    )}
                  </div>

                  {/* PRIORITY DISTRIBUTION */}

                  <div className="chart-card">
                    <div className="chart-heading">
                      <h2>Priority Distribution</h2>
                      <p>Data queue items grouped by priority</p>
                    </div>

                    {queue.length === 0 ? (
                      <div className="empty-message">
                        No queue data available.
                      </div>
                    ) : (
                      <ResponsiveContainer width="100%" height={280}>
                        <PieChart>
                          <Pie
                            data={priorityChartData.filter(
                              (item) => item.count > 0
                            )}
                            dataKey="count"
                            nameKey="priority"
                            cx="50%"
                            cy="50%"
                            outerRadius={90}
                            label={({ name, value }) =>
                              `${name}: ${value}`
                            }
                          >
                            {priorityChartData
                              .filter((item) => item.count > 0)
                              .map((item) => (
                                <Cell
                                  key={item.priority}
                                  fill={priorityColors[item.priority]}
                                />
                              ))}
                          </Pie>

                          <Tooltip />
                          <Legend />
                        </PieChart>
                      </ResponsiveContainer>
                    )}
                  </div>

                </section>

                <section className="panel">
                  <div className="panel-header">
                    <div>
                      <h2>Recent Telemetry</h2>
                      <p>Latest satellite telemetry records</p>
                    </div>

                    <button
                      className="text-button"
                      onClick={() => setActivePage("Telemetry")}
                    >
                      View all →
                    </button>
                  </div>

                  <TelemetryTable
                    telemetry={telemetry.slice(0, 5)}
                    getStatusClass={getStatusClass}
                  />
                </section>
              </>
            )}
          </>
        )}

        {activePage === "Telemetry" && (
          <section className="panel">
            <div className="panel-header">
              <div>
                <h2>Telemetry Records</h2>
                <p>All records received from satellites</p>
              </div>
            </div>

            {loading ? (
              <div className="loading-message">Loading...</div>
            ) : (
              <TelemetryTable
                telemetry={telemetry}
                getStatusClass={getStatusClass}
              />
            )}
          </section>
        )}

        {activePage === "Add Telemetry" && (
          <section className="panel form-panel">
            <div className="panel-header">
              <div>
                <h2>Add Satellite Telemetry</h2>
                <p>Enter new telemetry information for a satellite</p>
              </div>
            </div>

            <form onSubmit={handleTelemetrySubmit}>
              <div className="form-grid">

                <div className="form-group">
                  <label>Satellite ID</label>
                  <input
                    type="text"
                    name="satelliteId"
                    placeholder="Example: SAT-001"
                    value={formData.satelliteId}
                    onChange={handleFormChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Battery Level (%)</label>
                  <input
                    type="number"
                    name="batteryLevel"
                    placeholder="Enter battery level"
                    min="0"
                    max="100"
                    value={formData.batteryLevel}
                    onChange={handleFormChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Link Quality (%)</label>
                  <input
                    type="number"
                    name="linkQuality"
                    placeholder="Enter link quality"
                    min="0"
                    max="100"
                    value={formData.linkQuality}
                    onChange={handleFormChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Data Type</label>
                  <select
                    name="dataType"
                    value={formData.dataType}
                    onChange={handleFormChange}
                  >
                    <option value="SENSOR">SENSOR</option>
                    <option value="IMAGE">IMAGE</option>
                    <option value="EMERGENCY">EMERGENCY</option>
                    <option value="HOUSEKEEPING">HOUSEKEEPING</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Urgency</label>
                  <select
                    name="urgency"
                    value={formData.urgency}
                    onChange={handleFormChange}
                  >
                    <option value="LOW">LOW</option>
                    <option value="MEDIUM">MEDIUM</option>
                    <option value="HIGH">HIGH</option>
                    <option value="CRITICAL">CRITICAL</option>
                  </select>
                </div>

              </div>

              <div className="form-actions">
                <button
                  type="submit"
                  className="submit-button"
                  disabled={submitting}
                >
                  {submitting ? "Saving..." : "Add Telemetry"}
                </button>

                <button
                  type="button"
                  className="cancel-button"
                  onClick={() => {
                    setFormData({
                      satelliteId: "",
                      batteryLevel: "",
                      linkQuality: "",
                      dataType: "SENSOR",
                      urgency: "MEDIUM"
                    });
                    setError("");
                    setMessage("");
                  }}
                >
                  Clear Form
                </button>
              </div>
            </form>
          </section>
        )}

        {activePage === "Add Queue Item" && (
          <section className="panel form-panel">
            <div className="panel-header">
              <div>
                <h2>Add Data to Priority Queue</h2>
                <p>
                  Enter data details to receive an intelligent priority recommendation.
                </p>
              </div>
            </div>

            <form onSubmit={handleQueueSubmit}>
              <div className="form-grid">

                <div className="form-group">
                  <label>Satellite ID</label>
                  <input
                    type="text"
                    name="satelliteId"
                    placeholder="Example: SAT-001"
                    value={queueForm.satelliteId}
                    onChange={handleQueueFormChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Data Type</label>
                  <select
                    name="dataType"
                    value={queueForm.dataType}
                    onChange={handleQueueFormChange}
                  >
                    <option value="EMERGENCY">EMERGENCY</option>
                    <option value="SENSOR">SENSOR</option>
                    <option value="IMAGE">IMAGE</option>
                    <option value="HOUSEKEEPING">HOUSEKEEPING</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Urgency</label>
                  <select
                    name="urgency"
                    value={queueForm.urgency}
                    onChange={handleQueueFormChange}
                  >
                    <option value="CRITICAL">CRITICAL</option>
                    <option value="HIGH">HIGH</option>
                    <option value="MEDIUM">MEDIUM</option>
                    <option value="LOW">LOW</option>
                  </select>
                </div>

                <div className="form-group">
                  <label>Battery Level (%)</label>
                  <input
                    type="number"
                    name="batteryLevel"
                    placeholder="0 - 100"
                    min="0"
                    max="100"
                    value={queueForm.batteryLevel}
                    onChange={handleQueueFormChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label>Link Quality (%)</label>
                  <input
                    type="number"
                    name="linkQuality"
                    placeholder="0 - 100"
                    min="0"
                    max="100"
                    value={queueForm.linkQuality}
                    onChange={handleQueueFormChange}
                    required
                  />
                </div>

              </div>

              <div className="form-actions">
                <button
                  type="submit"
                  className="submit-button"
                  disabled={queueSubmitting}
                >
                  {queueSubmitting
                    ? "Calculating Priority..."
                    : "Add to Priority Queue"}
                </button>

                <button
                  type="button"
                  className="cancel-button"
                  onClick={() => {
                    setQueueForm({
                      satelliteId: "",
                      dataType: "SENSOR",
                      urgency: "MEDIUM",
                      batteryLevel: "",
                      linkQuality: ""
                    });
                    setError("");
                    setMessage("");
                  }}
                >
                  Clear Form
                </button>
              </div>
            </form>
          </section>
        )}


        {activePage === "Priority Queue" && (
          <section className="panel">
            <div className="panel-header">
              <div>
                <h2>Intelligent Data-Priority Queue</h2>
                <p>
                  Data items ranked using the priority engine
                </p>
              </div>

              <button className="text-button" onClick={fetchQueue}>
                ↻ Refresh Queue
              </button>
            </div>

            {queueLoading ? (
              <div className="loading-message">
                Loading priority queue...
              </div>
            ) : queue.length === 0 ? (
              <div className="empty-message">
                No queue items found. Add data using POST /api/queue.
              </div>
            ) : (
              <div className="table-container">
                <table>
                  <thead>
                    <tr>
                      <th>Satellite</th>
                      <th>Data Type</th>
                      <th>Urgency</th>
                      <th>Battery</th>
                      <th>Link</th>
                      <th>Score</th>
                      <th>Priority</th>
                      <th>Status</th>
                      <th>Action</th>
                    </tr>
                  </thead>

                  <tbody>
                    {queue.map((item) => {
                      const isHold =
                        item.priority === "HOLD" ||
                        item.transmissionStatus === "ON_HOLD";

                      const isTransmitted =
                        item.transmissionStatus === "TRANSMITTED";

                      return (
                        <tr key={item.id}>
                          <td className="satellite-id">
                            {item.satelliteId}
                          </td>
                          <td>{item.dataType}</td>
                          <td>{item.urgency}</td>
                          <td>{item.batteryLevel}%</td>
                          <td>{item.linkQuality}%</td>
                          <td>
                            {Number(item.priorityScore).toFixed(2)}
                          </td>
                          <td>
                            <span className={getPriorityClass(item.priority)}>
                              {item.priority}
                            </span>
                          </td>
                          <td>
                            <span
                              className={getStatusClass(
                                item.transmissionStatus
                              )}
                            >
                              {item.transmissionStatus}
                            </span>
                          </td>
                          <td>
                            {isHold ? (
                              <button className="blocked-button" disabled>
                                HOLD
                              </button>
                            ) : isTransmitted ? (
                              <button className="completed-button" disabled>
                                Completed
                              </button>
                            ) : (
                              <button
                                className="transmit-button"
                                disabled={transmittingId === item.id}
                                onClick={() => transmitItem(item.id)}
                              >
                                {transmittingId === item.id
                                  ? "Sending..."
                                  : "Transmit"}
                              </button>
                            )}
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            )}
          </section>
        )}

          {activePage === "Transmission History" && (
            <section className="panel">
              <div className="panel-header">
                <div>
                  <h2>Transmission History</h2>
                  <p>Review previous transmission decisions and results</p>
                </div>

                <button
                  className="text-button"
                  onClick={fetchHistory}
                >
                  ↻ Refresh History
                </button>
              </div>

              {history.length === 0 ? (
                <div className="empty-message">
                  No transmission history found.
                  Transmit an eligible queue item to create a record.
                </div>
              ) : (
                <div className="table-container">
                  <table>
                    <thead>
                      <tr>
                        <th>Satellite</th>
                        <th>Data Type</th>
                        <th>Priority</th>
                        <th>Score</th>
                        <th>Status</th>
                        <th>Transmission Time</th>
                        <th>Decision</th>
                      </tr>
                    </thead>

                    <tbody>
                      {history.map((item) => (
                        <tr key={item.id}>
                          <td className="satellite-id">
                            {item.satelliteId}
                          </td>

                          <td>{item.dataType}</td>

                          <td>
                            <span className={getPriorityClass(item.priority)}>
                              {item.priority}
                            </span>
                          </td>

                          <td>
                            {Number(item.priorityScore).toFixed(2)}
                          </td>

                          <td>
                            <span className={getStatusClass(item.transmissionStatus)}>
                              {item.transmissionStatus}
                            </span>
                          </td>

                          <td>
                            {item.transmissionTime
                              ? new Date(item.transmissionTime).toLocaleString()
                              : "Not available"}
                          </td>

                          <td>{item.decision}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </section>
          )}


        <footer className="footer">
          Satellite Telemetry Dashboard · Prototype System
        </footer>
      </main>
    </div>
  );
}

function TelemetryTable({ telemetry, getStatusClass }) {
  if (telemetry.length === 0) {
    return (
      <div className="empty-message">
        No telemetry records found.
      </div>
    );
  }

  return (
    <div className="table-container">
      <table>
        <thead>
          <tr>
            <th>Satellite ID</th>
            <th>Battery</th>
            <th>Link Quality</th>
            <th>Data Type</th>
            <th>Urgency</th>
            <th>Status</th>
          </tr>
        </thead>

        <tbody>
          {telemetry.map((item) => (
            <tr key={item.id}>
              <td className="satellite-id">{item.satelliteId}</td>
              <td>{item.batteryLevel}%</td>
              <td>{item.linkQuality}%</td>
              <td>{item.dataType}</td>
              <td>{item.urgency}</td>
              <td>
                <span className={getStatusClass(item.status)}>
                  {item.status || "UNKNOWN"}
                </span>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default App;
